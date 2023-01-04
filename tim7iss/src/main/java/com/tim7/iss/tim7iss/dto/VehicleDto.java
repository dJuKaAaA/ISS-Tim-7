package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    private Long id;
    private Long driverId;
    @Pattern(regexp = "^STANDARD|LUXURY|VAN$", message = "Invalid vehicle type... Must be STANDARD, LUXURY or VAN")
    private String vehicleType;
    @NotBlank(message = "Model should be provided")
    private String model;
    @NotBlank(message = "License number should be provided")
    private String licenseNumber;
    @NotNull(message = "Vehicle should have a current location")
    private GeoCoordinateDto currentLocation;
    @Min(value = 1, message = "Vehicle must have at least 1 passenger seat")
    @Max(value = 5, message = "Vehicle cannot have more than 5 passenger seats")
    private Integer passengerSeats;
    @NotNull(message = "Baby transport must be provided")
    private Boolean babyTransport;
    @NotNull(message = "Pet transport must be provided")
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
