package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
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
