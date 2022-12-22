package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationRequestDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs.RideRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "Price cannot be a negative number")
    private int price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Min(value = 0, message = "Estimated time in minutes cannot be a negative number")
    private Integer estimatedTimeInMinutes;
    private boolean babyOnBoard;
    private boolean petOnBoard;
    private boolean splitFare;
    private Enums.RideStatus status;

    @NotNull(message = "Driver is mandatory")
    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @NotNull(message = "Vehicle type is mandatory")
    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @NotEmpty(message = "There must be at least 1 passenger assigned to a ride")
    @ManyToMany(mappedBy = "rides")
    private Set<Passenger> passengers = new HashSet<>();

    @OneToOne(mappedBy = "ride", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private Refusal refusal;

    @NotEmpty(message = "There must be at least 1 route for the driver to traverse")
    @ManyToMany
    @JoinTable(
            name = "ride_routes",
            joinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "route_id", referencedColumnName = "id")
    )
    private Set<Route> routes = new HashSet<>();

    public Ride(Passenger passenger){
        this.passengers.add(passenger);
    }

    public Ride(RideRequestDTO rideRequestDTO) {
        for (LocationRequestDTO location : rideRequestDTO.locations) {
            Route r = new Route(new Location(location.departure), new Location(location.destination));
            this.routes.add(r);
        }
//        this.vehicleType = new VehicleType();
//        this.vehicleType.setVehicleName(rideRequestDTO.vehicleType);
        this.babyOnBoard = rideRequestDTO.babyTransport;
        this.petOnBoard = rideRequestDTO.petTransport;
        this.status = Enums.RideStatus.PENDING;
    }

}

