package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.requestDTOs.LocationRequestDTO;
import com.tim7.iss.tim7iss.requestDTOs.RideRequestDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RideResponseDTO {
    public String startTime;
    public String endTime;
    public Integer totalCost;

    public RideUserDTO driver;
    public Set<RideUserDTO> passengers = new HashSet<>();
    public Integer estimatedTimeInMinutes;
    public Enums.VehicleName vehicleType;
    public Boolean babyTransport;
    public Boolean petTransport;
    public List<LocationResponseDTO> locations = new ArrayList<>();
    public Enums.RideStatus status;

    public RideResponseDTO(RideRequestDTO ride){
        this.startTime = String.valueOf(LocalDateTime.now());
        this.endTime = String.valueOf(LocalDateTime.now());
        this.totalCost = 1000;
        this.estimatedTimeInMinutes = 20;
        this.vehicleType = ride.vehicleType;
        this.babyTransport = ride.babyTransport;
        this.petTransport = ride.petTransport;
        for(LocationRequestDTO locationDTO : ride.locations) {
            this.locations.add(locationDTO.destination);
        }
        this.status = Enums.RideStatus.PENDING;
    }

    public RideResponseDTO(Ride ride){
        this.startTime = String.valueOf(ride.getStartDate());
        this.endTime =  String.valueOf(ride.getEndDate());
        this.totalCost = ride.getPrice();
        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes();
        if(ride.getVehicleType() == null)
            this.vehicleType = null;
        else
            this.vehicleType = ride.getVehicleType().getVehicleName();
        this.babyTransport = ride.isBabyOnBoard();
        this.petTransport = ride.isPetOnBoard();
        try {
            for (Location location : ride.getRoute().getEndPoints()) {
                this.locations.add(new LocationResponseDTO(location));
            }
        }catch (Exception ex){
        }
        this.status = ride.getStatus();
        try {
            for (Passenger passenger : ride.getPassengers()) {
                this.passengers.add(new RideUserDTO(passenger, "PASSENGER"));
            }
        }catch (Exception ex){
        }
        this.driver = new RideUserDTO(ride.getDriver(), "DRIVER");
    }

    public void addPassengers(Set<Passenger>passengers){
        for(Passenger passenger : passengers){
            this.passengers.add(new RideUserDTO(passenger,"PASSENGER"));
        }
    }
}
