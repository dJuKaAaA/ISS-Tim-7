package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    public Vehicle(String model, String registrationPlate, int seatNumber, boolean babyAllowed, boolean petsAllowed, Driver driver, Location location) {
        this.model = model;
        this.registrationPlate = registrationPlate;
        this.seatNumber = seatNumber;
        this.babyAllowed = babyAllowed;
        this.petsAllowed = petsAllowed;
        this.driver = driver;
        this.location = location;
    }
}
