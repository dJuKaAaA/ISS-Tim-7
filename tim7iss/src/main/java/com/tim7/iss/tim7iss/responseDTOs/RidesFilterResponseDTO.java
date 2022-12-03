package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class RidesFilterResponseDTO {
    private Long id;
    private int totalCost;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime estimatedTimeInMinutes;
    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private Set<Location> locations;
    private RideUserDTO driver;
    private Set<RideUserDTO> passengers;

    public RidesFilterResponseDTO(Ride ride) {
        this.id = ride.getId();
        this.totalCost = ride.getPrice();
        this.startTime = ride.getStartDate();
        this.endTime = ride.getEndDate();
        this.estimatedTimeInMinutes = ride.getEstimatedTime();
        this.vehicleType = ride.getVehicleType();
        this.babyTransport = ride.isBabyOnBoard();
        this.petTransport = ride.isPetOnBoard();
        this.locations = new HashSet<>();
        getLocations(ride.getRoute());
        this.driver = new RideUserDTO(ride.getDriver(), "DRIVER");
        this.passengers = new HashSet<>();
        getPassengers(ride.getPassengers());
    }

    public void getLocations(Route route){
        if(route == null) return;
        for(Location location : route.getEndPoints()){
            this.locations.add(location);
        }
    }

    public void getPassengers(Set<Passenger> passengers){
        for(Passenger passenger : passengers){
            this.passengers.add(new RideUserDTO(passenger,"PASSENGER"));
        }
    }
}
