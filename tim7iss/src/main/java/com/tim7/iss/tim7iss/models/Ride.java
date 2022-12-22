package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationRequestDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs.RideRequestDTO;
import com.tim7.iss.tim7iss.dto.GeoCoordinateDto;
import com.tim7.iss.tim7iss.dto.LocationsForRideDto;
import com.tim7.iss.tim7iss.dto.RideCreationDto;
import com.tim7.iss.tim7iss.dto.RideDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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

    private int price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer estimatedTimeInMinutes;
    private boolean babyOnBoard;
    private boolean petOnBoard;
    private boolean splitFare;
    private Enums.RideStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @ManyToMany(mappedBy = "finishedRides")
    private Set<Passenger> passengers = new HashSet<>();

    @OneToOne(mappedBy = "ride", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private Refusal refusal;

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

    public Ride(RideCreationDto rideRequestDTO) {
        for (LocationsForRideDto location : rideRequestDTO.getLocations()) {
            Route r = new Route(new Location(location.getDeparture()), new Location(location.getDestination()));
            this.routes.add(r);
        }
//        this.vehicleType = new VehicleType();
//        this.vehicleType.setVehicleName(rideRequestDTO.vehicleType);
        this.babyOnBoard = rideRequestDTO.getBabyTransport();
        this.petOnBoard = rideRequestDTO.getPetTransport();
        this.status = Enums.RideStatus.PENDING;
    }

}

