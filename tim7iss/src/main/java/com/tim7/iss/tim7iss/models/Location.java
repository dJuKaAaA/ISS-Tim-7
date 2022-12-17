package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.LocationDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationResponseDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
