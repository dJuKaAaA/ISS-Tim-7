package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.GeoCoordinateDto;
import jakarta.persistence.*;
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

    private String name;

    // TODO: Make allowed values from -180 to 180
    private double longitude;

    // TODO: Make allowed values from -90 to 90
    private double latitude;


    public Location(String name, double longitude, double latitude) {
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
