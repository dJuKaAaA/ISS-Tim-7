package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideCreationDto {

    private List<LocationsForRideDto> locations;
    private List<UserRefDto> passengers;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

}
