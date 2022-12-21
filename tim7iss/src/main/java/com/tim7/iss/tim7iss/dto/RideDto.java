package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String startTime;
    private String endTime;
    private Double totalCost;
    private UserRefDto driver;
    private List<UserRefDto> passengers;
    private Integer estimatedTimeInMinutes;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;
    private RideRejectDto rejection;
    private List<LocationsForRideDto> locations;

}
