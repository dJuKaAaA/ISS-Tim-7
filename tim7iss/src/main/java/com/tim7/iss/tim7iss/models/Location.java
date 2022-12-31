package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.GeoCoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    private float latitude;
    private float longitude;


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

    @Override
    public Location clone() {
        try {
            Location clone = (Location) super.clone();
            this.setId(null);
            clone.setName(this.name);
            clone.setLongitude(this.longitude);
            clone.setLatitude(this.latitude);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
