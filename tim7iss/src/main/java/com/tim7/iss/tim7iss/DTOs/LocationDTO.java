package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    private String address;
    private double latitude;
    private double longitude;

    public LocationDTO(Location location) {
        this.address = "Neka adresa";  // TODO: Extract address name from map using latitude and longitude
        this.latitude = location.getX();
        this.longitude = location.getY();
    }

}
