package me.oblomov.househunter.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import me.oblomov.househunter.db.dao.HouseDAO;
import me.oblomov.househunter.db.dao.HouseFilterDAO;
import me.oblomov.househunter.db.model.HouseEntity;
import me.oblomov.househunter.db.model.HouseFilterEntity;
import me.oblomov.househunter.dto.HouseDetail;
import me.oblomov.househunter.dto.HouseList;
import me.oblomov.househunter.dto.ItemListElement;
import me.oblomov.househunter.enums.SortOrder;
import me.oblomov.househunter.service.builder.HouseHuntBuilder;
import org.apache.commons.text.StringSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class HouseHunterService {

    private static final Logger log = LoggerFactory.getLogger(HouseHunterService.class);
    private final static ObjectMapper objectMapper;

    public final static String BASE_URL_FOR_SALE = "https://www.funda.nl/koop/";
    private final HouseFilterDAO houseFilterDAO;
    private final HouseDAO houseDAO;

    private final MailSenderService mailSenderService;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    public HouseHunterService(HouseFilterDAO houseFilterDAO, HouseDAO houseDAO, MailSenderService mailSenderService) {
        this.houseFilterDAO = houseFilterDAO;
        this.houseDAO = houseDAO;
        this.mailSenderService = mailSenderService;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    //@Scheduled(cron = "0 0 * * * ?") // every hour
    public void scanForNewAdverts() {
        log.info("Scanning for new adverts");
        List<HouseFilterEntity> filters = houseFilterDAO.findAll();
        if(filters.isEmpty()) {
            HouseFilterEntity houseFilter = HouseFilterEntity.of(
                    BASE_URL_FOR_SALE,
                    "Eindhoven",
                    "blixembosch-oost",
                    0L,
                    600000L,
                    SortOrder.DESC_DATE);
            filters.add(houseFilterDAO.save(houseFilter));
        }
        filters.forEach(filter -> {
            try {
                Optional<HouseList> houseListOptional = getHouseList(filter);
                if (houseListOptional.isPresent()) {
                    HouseList houseList = houseListOptional.get();
                    log.info("URLs of houses: {}",
                            houseList.getItemListElement().stream()
                                    .map(ItemListElement::getUrl)
                                    .collect(Collectors.joining(", ")));

                    List<String> urls = houseList.getItemListElement().stream().map(ItemListElement::getUrl).collect(Collectors.toList());
                    List<HouseEntity> newHouses = findNewAdverts(urls);
                    log.info("New houses: {}", newHouses);
                    notify(filter, newHouses);
                    houseDAO.saveAll(newHouses);
                } else {
                    log.error("Could not get house list for filter: {}", filter);
                }
            } catch (Exception e) {
                log.error("Could not retrieve house list for filter {}", filter, e);
            }
        });
    }

    private void notify(HouseFilterEntity filter, List<HouseEntity> newHouses) {
        if(newHouses.isEmpty()) {
            log.info("No new houses found");
            return;
        }
        final String tableHeader = """
                        <tr>
                            <th>Advert Title</th>
                            <th>District</th>
                            <th>Date</th>
                            <th>Price</th>
                        </tr>
                    """;
        final String tableBodyPattern = """
                        <tr>
                            <td><a href="${URL}">${TITLE}</a></td>
                            <td>${DISTRICT}</td>
                            <th>${DATE}</th>
                            <td>€${PRICE}</td>
                        </tr>
                    """;
        String tableBody = newHouses.stream().map(house -> {
            Map<String, String> tempPlaceHolders = new HashMap<>();
            tempPlaceHolders.put("URL", house.getUrl());
            tempPlaceHolders.put("TITLE", house.getStreet() + " - " + house.getHouseNumber());
            tempPlaceHolders.put("DISTRICT", house.getNeighborhood());
            tempPlaceHolders.put("DATE", house.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
            tempPlaceHolders.put("PRICE", String.valueOf(house.getPrice()));
            return StringSubstitutor.replace(tableBodyPattern, tempPlaceHolders);
        }).collect(Collectors.joining("\n"));

        Map<String, Object> mailParameters = new HashMap<>();
        mailParameters.put("TABLE_HEADER", tableHeader);
        mailParameters.put("TABLE_BODY", tableBody);
        mailParameters.put("NUMBER_OF_NEW_HOUSES", newHouses.size());
        mailParameters.put("PROVINCE", filter.getProvince());
        mailParameters.put("DISTRICT", filter.getDistrict());

        mailSenderService.send(mailParameters);
    }

    private List<HouseEntity> findNewAdverts(List<String> urls) {
        List<HouseEntity> foundHouses = new ArrayList<>();
        for (String url : urls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.getElementsByAttributeValue("type", "application/ld+json");
                for (Element script : elements) {
                    String wholeData = ((DataNode) script.childNodes().get(0)).getWholeData();
                    try {
                        HouseDetail houseDetail = objectMapper.readValue(wholeData, HouseDetail.class);
                        if (houseDetail.getId() != null && !houseDetail.getId().isEmpty()) {
                            log.info("Found new advert: {}", houseDetail.getId());
                            Elements advertDateElements = doc.getElementsMatchingOwnText("Aangeboden sinds");
                            if (advertDateElements.size() > 0 && advertDateElements.get(0).parent() != null) {
                                LocalDate date = findAndParseAdvertDate(advertDateElements.get(0).parent());
                                if (date != null) {
                                    log.info("Found date: {}", date);
                                    HouseEntity found = HouseEntity.builder()
                                            .id(Long.parseLong(houseDetail.getId()))
                                            .date(date)
                                            .postcode(houseDetail.getPostcode())
                                            .city(houseDetail.getCity())
                                            .neighborhood(houseDetail.getNeighborhood())
                                            .street(extractStreetNameFromUrl(url, houseDetail))
                                            .houseNumber(houseDetail.getHouseNumber())
                                            .livingArea(houseDetail.getLivingArea())
                                            .price(houseDetail.getPrice())
                                            .numberOfRooms(houseDetail.getNumberOfRooms())
                                            .energyClass(houseDetail.getEnergyClass())
                                            .garden(houseDetail.hasGarden())
                                            .balcony(houseDetail.hasBalcony())
                                            .priceRange(houseDetail.getPriceRange())
                                            .yearOfConstruction(houseDetail.getYearOfConstruction())
                                            .energyEfficient(houseDetail.isEnergyEfficient())
                                            .status(houseDetail.getStatus())
                                            .url(url).build();
                                    foundHouses.add(found);
                                } else {
                                    log.info("Could not find date: {}", url);
                                }
                            }
                        }
                    } catch (Exception ignore) {
                        // Keep searching right data
                    }
                }
            } catch (IOException e) {
                log.error("Could not get document from url: {}", url, e);
            }
        }

        List<HouseEntity> alreadyAddedHouses = houseDAO.findAllById(foundHouses.stream().map(HouseEntity::getId).collect(Collectors.toList()));
        foundHouses.removeAll(alreadyAddedHouses);

        return foundHouses;
    }

    private static String extractStreetNameFromUrl(String url, HouseDetail houseDetail) {
        String[] split = url.split("/");
        String houseUriPath = split[split.length - 1];
        String street = houseUriPath.substring(houseUriPath.indexOf(houseDetail.getId() + "-") + (houseDetail.getId() + "-").length());
        street = street.substring(0, street.indexOf("-" + houseDetail.getHouseNumber()));
        return street;
    }

    private LocalDate findAndParseAdvertDate(Element parent) {
        Elements allElements = parent.getAllElements();
        for (Element element : allElements) {
            if (element.hasText()) {
                try {
                    Locale locale = new Locale("nl", "NL");
                    DateTimeFormatter f =
                            DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale);
                    return LocalDate.parse(element.text(), f);
                } catch (Exception ignored) {
                    // Keep searching
                }
            }
        }
        return null;
    }


    public Optional<HouseList> getHouseList(HouseFilterEntity filter) {
        try {
            String url = new HouseHuntBuilder()
                    .baseUrl(BASE_URL_FOR_SALE)
                    .fromFilter(filter)
                    .build();
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.getElementsByAttributeValue("type", "application/ld+json");
            for (Element script : elements) {
                String wholeData = ((DataNode) script.childNodes().get(0)).getWholeData();
                try {
                    HouseList houseList = objectMapper.readValue(wholeData, HouseList.class);
                    if (houseList.getItemListElement() != null) {
                        return Optional.of(houseList);
                    }
                } catch (Exception ignore) {
                    // Keep searching right data
                }
            }
        } catch (IOException e) {
            log.error("House list could not be retrieved. ", e);
        }
        return Optional.empty();
    }
}
