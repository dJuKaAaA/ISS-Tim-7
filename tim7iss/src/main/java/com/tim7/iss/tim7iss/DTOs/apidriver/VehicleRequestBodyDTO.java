package com.tim7.iss.tim7iss.DTOs.apidriver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequestBodyDTO {

    private String vehicleType;
    private String model;
    private String licenseNumber;
    private LocationDTO currentLocation;
    private int passengerSeats;
    private boolean babyTransport;
    private boolean petTransport;

}
