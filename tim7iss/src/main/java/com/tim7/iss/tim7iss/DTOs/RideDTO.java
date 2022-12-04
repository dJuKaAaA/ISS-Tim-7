package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RideDTO {

    private Long id;
    private Set<LocationDTO> locations = new HashSet<>();
    private String startDate;
    private String endDate;
    private double totalCost;
    DriverRidesDTO driver;
    Set<PassengerRidesDTO> passengers = new HashSet<>();
    private int estimatedTimeInMinutes;
    private Enums.RideStatus status;
    private boolean babyTransport;
    private boolean petTransport;

    public RideDTO(Ride ride) {
        this.id = ride.getId();

        Route routes = ride.getRoute();
        for(Location location: routes.getEndPoints()){
            LocationDTO locationDTO = new LocationDTO(location);
            this.locations.add(locationDTO);
        }
        this.startDate = ride.getStartDate().toString();
        this.endDate = ride.getEndDate().toString();
        this.totalCost = ride.getPrice();
        this.driver = new DriverRidesDTO(ride.getDriver());

        for(Passenger passenger : ride.getPassengers()){
            PassengerRidesDTO passengerRidesDTO = new PassengerRidesDTO(passenger);
            this.passengers.add(passengerRidesDTO);
        }

        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes(); // TODO promeniti
        this.status = ride.getStatus();
        this.babyTransport = ride.isBabyOnBoard();
        this.petTransport = ride.isPetOnBoard();
    }


}
