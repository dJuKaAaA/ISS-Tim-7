package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.requestDTOs.LocationRequestDTO;
import com.tim7.iss.tim7iss.requestDTOs.RideRequestDTO;
import com.tim7.iss.tim7iss.responseDTOs.LocationResponseDTO;
import com.tim7.iss.tim7iss.services.VehicleTypeService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
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

    @OneToMany(mappedBy = "ride")
    private Set<Message> messages = new HashSet<>();

    @ManyToMany(mappedBy = "finishedRides")
    private Set<Passenger> passengers = new HashSet<>();

    @OneToOne(mappedBy = "ride")
    private Refusal refusal;

    @OneToMany(mappedBy = "ride")
    private Set<Review> reviews = new HashSet<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    public Ride(Passenger passenger){
        this.passengers.add(passenger);
    }

    public Ride(RideRequestDTO rideRequestDTO){
        List<LocationResponseDTO>destinations = new ArrayList<>();
        for(LocationRequestDTO locationDTO : rideRequestDTO.locations) {
            destinations.add(locationDTO.destination);
        }
        this.route = new Route(rideRequestDTO.locations.get(0).departure,destinations);
//        this.vehicleType = new VehicleType();
//        this.vehicleType.setVehicleName(rideRequestDTO.vehicleType);
        this.babyOnBoard = rideRequestDTO.babyTransport;
        this.petOnBoard = rideRequestDTO.petTransport;
        this.status = Enums.RideStatus.PENDING;
    }
}

