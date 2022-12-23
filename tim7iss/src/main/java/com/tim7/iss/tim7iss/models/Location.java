package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.GeoCoordinateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueLatitudeAndLongitude", columnNames = { "latitude", "longitude" })})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Min(-180)
    @Max(180)
    private float longitude;

    @Min(-90)
    @Max(90)
    private float latitude;


    public Location(String name, float longitude, float latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(GeoCoordinateDto geoCoordinateDto) {
        this.name = geoCoordinateDto.getAddress();
        this.longitude = geoCoordinateDto.getLongitude();
        this.latitude = geoCoordinateDto.getLatitude();
    }

}
