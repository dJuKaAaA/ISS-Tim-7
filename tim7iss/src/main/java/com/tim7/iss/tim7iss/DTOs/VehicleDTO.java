package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    private Long id;
    private Long driverId;
    private String model;
    private String licenceNumber;
    private Location currentLocation;
    private int passengerSeats;
    private boolean babyTransport;
    private boolean petTransport;

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.driverId = vehicle.getDriver().getId();
        this.model = vehicle.getModel();
        this.licenceNumber = vehicle.getRegistrationPlate();
        this.currentLocation = vehicle.getLocation();
        this.passengerSeats = vehicle.getSeatNumber();
        this.babyTransport = vehicle.isBabyAllowed();
        this.petTransport = vehicle.isPetsAllowed();
    }

}
