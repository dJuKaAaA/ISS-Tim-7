package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteDTO {

    private LocationDTO departure;
    private LocationDTO destination;

    public RouteDTO(Route route){
        this.departure = new LocationDTO(route.getStartingPoint());
        this.destination = new LocationDTO(route.getEndPoint());
    }
}
