package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long driverId;
    private String vehicleType;
    private String model;
    private String licenseNumber;
    private GeoCoordinateDto currentLocation;
    private Integer passengerSeats;
    private Boolean babyTransport;
    private Boolean petTransport;


}
