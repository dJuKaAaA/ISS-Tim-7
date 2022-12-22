package com.tim7.iss.tim7iss.DTOs.apidriver;

import com.tim7.iss.tim7iss.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    private String address;
    private Float latitude;
    private Float longitude;

    public LocationDTO(Location location) {
        this.address = location.getName();  // TODO: Extract address name from map using latitude and longitude
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

}
