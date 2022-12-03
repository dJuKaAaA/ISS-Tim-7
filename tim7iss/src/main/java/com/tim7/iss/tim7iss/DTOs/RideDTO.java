package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Refusal;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDTO {

    private Long id;
    private List<LocationDTO> locations = new ArrayList<>();
    private Refusal refusal;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private DriverDTO driver;
    private Set<PassengerDTO> passengers = new HashSet<>();
    private int estimatedTimeInMinutes;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public RideDTO(Ride ride) {
        this.id = ride.getId();
        for (Route route : ride.getRoutes()) {
            this.locations.add(new LocationDTO(route.getEndPoint()));
        }
        this.startTime = ride.getStartDate();
        this.endTime = ride.getEndDate();
        this.totalCost = ride.getPrice();
        this.driver = new DriverDTO(ride.getDriver());
        ride.getPassengers().forEach(passenger -> this.passengers.add(new PassengerDTO(passenger)));
        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes();
        this.vehicleType = (ride.getVehicleType() == null) ? "I J*** TE BMW" : ride.getVehicleType().getName();
        this.babyTransport = ride.isBabyOnBoard();
        this.petTransport = ride.isPetOnBoard();
    }

}
