package com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocationRequestDTO {
    public LocationResponseDTO departure;
    public LocationResponseDTO destination;

    public LocationRequestDTO(LocationResponseDTO departure, LocationResponseDTO destination){
        this.departure = departure;
        this.destination = destination;
    }
}
