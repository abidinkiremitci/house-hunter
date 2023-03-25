package me.oblomov.househunter.db.model;

import jakarta.persistence.*;
import me.oblomov.househunter.enums.SortOrder;

@Entity
public class HouseFilterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "house_filter_generator")
    @SequenceGenerator(name = "house_filter_generator", sequenceName = "house_filter_seq")
    private Long id;
    private final String baseUrl;
    private final String province;
    private final String district;
    private final Long min;
    private final Long max;
    @Enumerated(EnumType.STRING)
    private final SortOrder sortOrder;


    public HouseFilterEntity() {
        baseUrl = null;
        province = null;
        district = null;
        min = -1L;
        max = -1L;
        sortOrder = null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private HouseFilterEntity(String baseUrl, String province, String district, Long min, Long max, SortOrder sortOrder) {
        this.baseUrl = baseUrl;
        this.province = province;
        this.district = district;
        this.min = min;
        this.max = max;
        this.sortOrder = sortOrder;
    }

    public static HouseFilterEntity of(String baseUrl,
                                       String province,
                                       String district,
                                       Long min,
                                       Long max,
                                       SortOrder sortOrder) {
        return new HouseFilterEntity(baseUrl, province, district, min, max, sortOrder);
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public Long getMin() {
        return min;
    }

    public Long getMax() {
        return max;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
