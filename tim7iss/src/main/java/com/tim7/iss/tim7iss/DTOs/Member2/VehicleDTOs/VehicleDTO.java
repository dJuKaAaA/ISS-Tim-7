package com.tim7.iss.tim7iss.DTOs.Member2.VehicleDTOs;

import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationResponseDTO;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.models.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehicleDTO {
    private boolean petTransport;
    private Long driverId;
    private VehicleType vehicleType;
    private String model;
    private String licenseNumber;
    private LocationResponseDTO currentLocation;
    private int passengerSeats;
    private boolean babyTransport;
    private Long id;

    public VehicleDTO(Vehicle vehicle){
        this.id = vehicle.getId();
        try {
            this.driverId = vehicle.getDriver().getId();
        }catch (Exception ex){
            this.driverId = null;
        }
        this.vehicleType = vehicle.getVehicleType();
        this.model = vehicle.getModel();
        this.licenseNumber = vehicle.getRegistrationPlate();
        this.currentLocation = new LocationResponseDTO(vehicle.getLocation());
        this.passengerSeats = vehicle.getSeatNumber();
        this.babyTransport = vehicle.isBabyAllowed();
        this.petTransport = vehicle.isPetsAllowed();
    }
}
