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

    private String startTime;
    private String endTime;
    private double totalCost;
    SimpleDriverDTO driver;
    Set<SimplePassengerDTO> passengers = new HashSet<>();
    private int estimatedTimeInMinutes;

    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    private RejectionDTO rejection;

    private Set<RouteDTO> locations = new HashSet<>();

    public RideDTO(Ride ride) {
        this.id = ride.getId();

        Set<Route> routes = ride.getRoutes();
        for(Route route: routes){
            RouteDTO routeDTO = new RouteDTO(route);
            this.locations.add(routeDTO);
        }
        this.startTime = ride.getStartTime().format(Constants.customDateTimeFormat);
        this.endTime = ride.getEndTime().format(Constants.customDateTimeFormat);
        this.totalCost = ride.getPrice();
        this.driver = new SimpleDriverDTO(ride.getDriver());

        for(Passenger passenger : ride.getPassengers()){
            SimplePassengerDTO passengerRidesDTO = new SimplePassengerDTO(passenger);
            this.passengers.add(passengerRidesDTO);
        }

        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes(); // TODO promeniti
        this.vehicleType = ride.getVehicleType().getName();
        this.babyTransport = ride.isBabyOnBoard();
        this.petTransport = ride.isPetOnBoard();
        this.rejection = new RejectionDTO(ride.getRefusal());
    }


}
