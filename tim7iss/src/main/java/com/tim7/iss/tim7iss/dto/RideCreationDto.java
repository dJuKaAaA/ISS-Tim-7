package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideCreationDto {

    private List<LocationsForRideDto> locations = new ArrayList<>();
    private List<UserRefDto> passengers = new ArrayList<>();
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;

}
