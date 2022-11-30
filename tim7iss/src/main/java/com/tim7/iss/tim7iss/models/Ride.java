package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime estimatedTime;
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

    @ManyToMany(mappedBy = "ongoingRide")
    private Set<Passenger> passengers = new HashSet<>();

    @OneToMany(mappedBy = "ride")
    private Set<Refusal> refusals = new HashSet<>();

    @OneToMany(mappedBy = "ride")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "ride")
    private Set<Route> routes = new HashSet<>();

}
