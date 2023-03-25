package me.oblomov.househunter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tinyid",
        "postcode",
        "plaats",
        "buurt",
        "huisnummer",
        "vraagprijs",
        "aantalkamers",
        "energieklasse",
        "tuin",
        "balkon",
        "vraagprijsrange",
        "bouwjaar",
        "openhuis",
        "energiezuinig",
        "status"
})
public class HouseDetail {

    @JsonProperty("tinyid")
    private String id;
    @JsonProperty("postcode")
    private String postcode;
    @JsonProperty("plaats")
    private String city;
    @JsonProperty("buurt")
    private String neighborhood;
    @JsonProperty("huisnummer")
    private String houseNumber;
    @JsonProperty("woonoppervlakte")
    private Long livingArea;
    @JsonProperty("vraagprijs")
    private Long price;
    @JsonProperty("aantalkamers")
    private Long numberOfRooms;
    @JsonProperty("energieklasse")
    private String energyClass;
    @JsonProperty("tuin")
    private Boolean garden;
    @JsonProperty("balkon")
    private Boolean balcony;
    @JsonProperty("vraagprijsrange")
    private Long priceRange;
    @JsonProperty("bouwjaar")
    private Long yearOfConstruction;
    @JsonProperty("energiezuinig")
    private Boolean energyEfficient;
    @JsonProperty("status")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Long getLivingArea() {
        return livingArea;
    }

    public void setLivingArea(String livingArea) {
        this.livingArea = Long.parseLong(livingArea);
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = Long.parseLong(price);
    }

    public Long getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = Long.parseLong(numberOfRooms);
    }

    public String getEnergyClass() {
        return energyClass;
    }

    public void setEnergyClass(String energyClass) {
        this.energyClass = energyClass;
    }

    public Boolean hasGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = Boolean.parseBoolean(garden);
    }

    public Boolean hasBalcony() {
        return balcony;
    }

    public void setBalcony(String balcony) {
        this.balcony = Boolean.parseBoolean(balcony);
    }

    public Long getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = Long.parseLong(priceRange);
    }

    public Long getYearOfConstruction() {
        return yearOfConstruction;
    }

    public void setYearOfConstruction(String yearOfConstruction) {
        this.yearOfConstruction = Long.parseLong(yearOfConstruction);
    }

    public Boolean isEnergyEfficient() {
        return energyEfficient;
    }

    public void setEnergyEfficient(String energyEfficient) {
        this.energyEfficient = Boolean.parseBoolean(energyEfficient);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
