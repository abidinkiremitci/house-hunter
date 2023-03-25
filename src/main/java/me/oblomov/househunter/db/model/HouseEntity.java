package me.oblomov.househunter.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "neighborhood", "street", "houseNumber"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HouseEntity {
    @Id
    private Long id;
    private LocalDate date;
    private String postcode;
    private String city;
    private String neighborhood;
    private String street;
    private String houseNumber;
    private Long livingArea;
    private Long price;
    private Long numberOfRooms;
    private String energyClass;
    private Boolean garden;
    private Boolean balcony;
    private Long priceRange;
    private Long yearOfConstruction;
    private Boolean energyEfficient;
    private String status;
    private String url;
}
