package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrivingAssessmentDto {

    private List<LocationForRideDto> locations;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;

}
