package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String registrationPlate;
    private int seatNumber;
    private boolean babyAllowed;
    private boolean petsAllowed;

    @OneToOne(mappedBy = "vehicle")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL)
    private Set<VehicleReview> reviews;



}
