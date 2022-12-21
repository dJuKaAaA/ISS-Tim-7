package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationsForRideDto {

    private GeoCoordinateDto departure;
    private GeoCoordinateDto destination;

    public LocationsForRideDto(Route route) {
        this.departure = new GeoCoordinateDto(route.getStartingPoint());
        this.destination = new GeoCoordinateDto(route.getEndPoint());
    }

}
