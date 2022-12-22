package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double longitude;
    private double latitude;


    public Location(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(LocationResponseDTO locationResponseDTO) {
        this.name = locationResponseDTO.getAddress();
        this.longitude = locationResponseDTO.getLongitude();
        this.latitude = locationResponseDTO.getLatitude();
    }

    public Location(com.tim7.iss.tim7iss.DTOs.apidriver.LocationDTO locationDTO) {
        this.name = locationDTO.getAddress();
        this.longitude = locationDTO.getLongitude();
        this.latitude = locationDTO.getLatitude();
    }
}
