package me.oblomov.househunter.service.builder;

import me.oblomov.househunter.enums.SortOrder;
import me.oblomov.househunter.db.model.HouseFilterEntity;
import org.apache.logging.log4j.util.Strings;

public class HouseHuntBuilder {

    private String baseUrl;
    private String province;
    private String district;
    private Long min;
    private Long max;
    private SortOrder sortOrder;

    public HouseHuntBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
    public HouseHuntBuilder fromFilter(HouseFilterEntity filter) {
        return this.province(filter.getProvince())
                .baseUrl(filter.getBaseUrl())
                .district(filter.getDistrict())
                .min(filter.getMin())
                .max(filter.getMax())
                .sortOrder(filter.getSortOrder());
    }

    private HouseHuntBuilder min(Long min) {
        this.min = min;
        return this;
    }

    private HouseHuntBuilder max(Long max) {
        this.max = max;
        return this;
    }

    private HouseHuntBuilder district(String district) {
        this.district = district;
        return this;
    }

    private HouseHuntBuilder province(String province) {
        this.province = province;
        return this;
    }

    private HouseHuntBuilder sortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public String build() {
        StringBuilder urlBuilder = new StringBuilder()
                .append(baseUrl).append("/")
                .append(province).append("/");
        if(Strings.isNotEmpty(district)) {
            urlBuilder.append(district).append("/");
        }
        if(min != null && max != null && min<max) {
            urlBuilder.append(min).append("-").append(max).append("/");
        }
        if(sortOrder != null) {
            urlBuilder.append(sortOrder.getUriPath()).append("/");
        }
        return urlBuilder.toString();
    }

}
