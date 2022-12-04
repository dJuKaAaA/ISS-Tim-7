package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationDTO {
    private String address;
    private double latitude;
    private double longitude;

    public LocationDTO(Location location){
        this.address = location.getName();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
