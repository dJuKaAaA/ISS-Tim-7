package com.tim7.iss.tim7iss.DTOs.apidriver;

import com.tim7.iss.tim7iss.DTOs.Member2.PassengerDTOs.PassengerResponseDTO;
import com.tim7.iss.tim7iss.DTOs.RejectionDTO;
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
public class RideResponseDTO {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalCost;
    private DriverResponseDTO driver;
    private Set<PassengerResponseDTO> passengers = new HashSet<>();
    private int estimatedTimeInMinutes;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private RejectionDTO rejection;
    private List<LocationDTO> locations = new ArrayList<>();

    public RideResponseDTO(Ride ride) {
        this.id = ride.getId();

        // gets route from the ride, ride has a set of end points
        // set doesn't have get method, so it is converted to a list (list has a get method)
        // then using the size of the set the final endpoint is used for the location
        if (ride.getRoutes() != null) {  // TODO: Delete later
            for (Route route : ride.getRoutes()) {
                this.locations.add(new LocationDTO(route.getStartingPoint()));
                this.locations.add(new LocationDTO(route.getEndPoint()));
            }
        }
        this.startTime = ride.getStartDate();
        this.endTime = ride.getEndDate();
        this.totalCost = ride.getPrice();
        this.driver = new DriverResponseDTO(ride.getDriver());
        ride.getPassengers().forEach(passenger -> this.passengers.add(new PassengerResponseDTO(passenger)));
        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes();
        this.vehicleType = (ride.getVehicleType() == null) ? "" : ride.getVehicleType().getName();
        this.babyTransport = ride.isBabyOnBoard();
        this.petTransport = ride.isPetOnBoard();
        this.rejection = null;
    }

}
