package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoCoordinateDto {

    private String address;
    private Float latitude;
    private Float longitude;

    public GeoCoordinateDto(Location location) {
        this.address = location.getName();
        this.latitude = (float) location.getLatitude();  // TODO: Delete casting when latitude gets refactored to type float in Location class
        this.longitude = (float) location.getLongitude();  // TODO: Delete casting when longitude gets refactored to type float Location class
    }

}
