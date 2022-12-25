package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    private Long id;
    private Long driverId;
    private String vehicleType;
    private String model;
    private String licenseNumber;
    private GeoCoordinateDto currentLocation;
    private Integer passengerSeats;
    private Boolean babyTransport;
    private Boolean petTransport;

    public VehicleDto(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.driverId = vehicle.getDriver() == null ? null : vehicle.getDriver().getId();
        this.vehicleType = vehicle.getVehicleType().getName();
        this.model = vehicle.getModel();
        this.licenseNumber = vehicle.getRegistrationPlate();
        this.currentLocation = new GeoCoordinateDto(vehicle.getLocation());
        this.passengerSeats = vehicle.getSeatNumber();
        this.babyTransport = vehicle.isBabyAllowed();
        this.petTransport = vehicle.isPetsAllowed();
    }

}
