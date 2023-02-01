package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.LocationForRideDto;
import com.tim7.iss.tim7iss.dto.RideCreationDto;
import com.tim7.iss.tim7iss.global.Constants;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "passenger_rides",
            joinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    )
    private Set<Passenger> passengers = new HashSet<>();

    @OneToOne(mappedBy = "ride", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private Refusal refusal;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "ride_routes",
            joinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "route_id", referencedColumnName = "id")
    )
    private List<Route> routes = new ArrayList<>();

    public Ride(Passenger passenger) {
        this.passengers.add(passenger);
    }

    public Ride(RideCreationDto rideRequestDTO) {
        this.estimatedTimeInMinutes = 0;
        Route r;
        for (LocationForRideDto location : rideRequestDTO.getLocations()) {
            if(location.getEstimatedTimeInMinutes() == null)
                r = new Route(new Location(location.getDeparture()), new Location(location.getDestination()));
            else
                r = new Route(
                    new Location(location.getDeparture()),
                    new Location(location.getDestination()),
                    location.getDistanceInMeters(),
                    location.getEstimatedTimeInMinutes());

            this.routes.add(r);
            if(location.getEstimatedTimeInMinutes()!=null)
                this.estimatedTimeInMinutes += location.getEstimatedTimeInMinutes();
        }
        if(rideRequestDTO.getScheduledTime() != null)
            this.startTime = LocalDateTime.parse(rideRequestDTO.getScheduledTime(), Constants.customDateTimeFormat);
        else {
            this.startTime = LocalDateTime.now().plusMinutes(Constants.vehicleWaitTimeInMinutes + 1);
        }
        this.babyOnBoard = rideRequestDTO.getBabyTransport();
        this.petOnBoard = rideRequestDTO.getPetTransport();
        this.status = Enums.RideStatus.PENDING;
    }

}

