package com.tim7.iss.tim7iss.DTOs.apidriver;

import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDTO {

    private Long id;
    private Long driverId;
    private String vehicleType;
    private String model;
    private String licenseNumber;
    private LocationDTO currentLocation;
    private int passengerSeats;
    private boolean babyTransport;
    private boolean petTransport;

    public VehicleResponseDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.driverId = vehicle.getDriver().getId();
        this.vehicleType = (vehicle.getVehicleType() == null) ? "STANDARDNO" : vehicle.getVehicleType().getName();
        this.model = vehicle.getModel();
        this.licenseNumber = vehicle.getRegistrationPlate();
        this.currentLocation = new LocationDTO(vehicle.getLocation());
        this.passengerSeats = vehicle.getSeatNumber();
        this.babyTransport = vehicle.isBabyAllowed();
        this.petTransport = vehicle.isPetsAllowed();
    }

}
