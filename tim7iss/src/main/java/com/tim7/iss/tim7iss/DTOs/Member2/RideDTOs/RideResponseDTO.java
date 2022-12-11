package com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs;

import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationResponseDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.PassengerDTOs.RideUserDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.RefusalDTOs.RefusalDTO;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationRequestDTO;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class RideResponseDTO {

    public Long id;
    public String startTime;
    public String endTime;
    public Integer totalCost;

    public RideUserDTO driver;
    public Set<RideUserDTO> passengers = new HashSet<>();
    public Integer estimatedTimeInMinutes;
    public String vehicleType;
    public Boolean babyTransport;
    public Boolean petTransport;
    public List<LocationRequestDTO> locations = new ArrayList<>();
    public Enums.RideStatus status;

    public RefusalDTO rejection = new RefusalDTO();

    public RideResponseDTO(RideRequestDTO ride){
        if(ride == null)
            return;
        this.startTime = String.valueOf(LocalDateTime.now());
        this.endTime = String.valueOf(LocalDateTime.now());
        this.totalCost = 1000;
        this.estimatedTimeInMinutes = 20;
        this.vehicleType = ride.vehicleType;
        this.babyTransport = ride.babyTransport;
        this.petTransport = ride.petTransport;
        this.locations = ride.locations;
        this.status = Enums.RideStatus.PENDING;
        this.passengers = ride.passengers;
        this.driver = new RideUserDTO();
    }

    public RideResponseDTO(Ride ride){
        if(ride == null)
            return;
        this.id = id;
        this.startTime = String.valueOf(ride.getStartTime());
        this.endTime =  String.valueOf(ride.getEndTime());
        this.totalCost = ride.getPrice();
        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes();
        if(ride.getVehicleType() == null)
            this.vehicleType = null;
        else
            this.vehicleType = ride.getVehicleType().getName();
        this.babyTransport = ride.isBabyOnBoard();
        this.petTransport = ride.isPetOnBoard();
        try {
            for (Route route : ride.getRoutes()) {
                this.locations.add(new LocationRequestDTO(new LocationResponseDTO(route.getStartingPoint()),new LocationResponseDTO(route.getEndPoint())));
            }
        }catch (Exception ex){
        }
        this.status = ride.getStatus();
        try {
            for (Passenger passenger : ride.getPassengers()) {
                this.passengers.add(new RideUserDTO(passenger));
            }
        }catch (Exception ex){
        }
        this.driver = new RideUserDTO(ride.getDriver());
    }

    public void addPassengers(Set<Passenger>passengers){
        for(Passenger passenger : passengers){
            this.passengers.add(new RideUserDTO(passenger));
        }
    }
}
