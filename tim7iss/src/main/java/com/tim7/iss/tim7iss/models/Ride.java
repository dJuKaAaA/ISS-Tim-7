package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationRequestDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs.RideRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "ride")
    private Set<Message> messages = new HashSet<>();

    @ManyToMany(mappedBy = "finishedRides")
    private Set<Passenger> passengers = new HashSet<>();

    @OneToOne(mappedBy = "ride")
    private Refusal refusal;

    @OneToMany(mappedBy = "ride")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "ride")
    private Set<Route> routes = new HashSet<>();

    public Ride(Passenger passenger){
        this.passengers.add(passenger);
    }

    public Ride(RideRequestDTO rideRequestDTO) {
        for (LocationRequestDTO location : rideRequestDTO.locations) {
            Route r = new Route(new Location(location.departure), new Location(location.destination));
            r.setRide(this);
            this.routes.add(r);
        }
//        this.vehicleType = new VehicleType();
//        this.vehicleType.setVehicleName(rideRequestDTO.vehicleType);
        this.babyOnBoard = rideRequestDTO.babyTransport;
        this.petOnBoard = rideRequestDTO.petTransport;
        this.status = Enums.RideStatus.PENDING;
    }
    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", price=" + price +
                ", startDate=" + startTime +
                ", endDate=" + endTime +
                ", estimatedTime=" + estimatedTimeInMinutes +
                ", babyOnBoard=" + babyOnBoard +
                ", petOnBoard=" + petOnBoard +
                ", status=" + status +
                '}';
    }
}

