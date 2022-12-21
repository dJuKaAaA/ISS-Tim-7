package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationsForRideDto {

    private GeoCoordinateDto departure;
    private GeoCoordinateDto destination;

}
