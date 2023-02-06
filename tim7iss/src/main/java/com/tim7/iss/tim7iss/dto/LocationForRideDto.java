package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationForRideDto {

    @NotNull(message = "Departure not provided")
    private GeoCoordinateDto departure;
    @NotNull(message = "Destination not provided")
    private GeoCoordinateDto destination;
    private Integer distanceInMeters;
    private Integer estimatedTimeInMinutes;


    public LocationForRideDto(Route route) {
        this.departure = new GeoCoordinateDto(route.getStartingPoint());
        this.destination = new GeoCoordinateDto(route.getEndPoint());
        this.distanceInMeters = route.getDistanceInMeters();
        this.estimatedTimeInMinutes = route.getEstimatedTimeInMinutes();
    }

}
