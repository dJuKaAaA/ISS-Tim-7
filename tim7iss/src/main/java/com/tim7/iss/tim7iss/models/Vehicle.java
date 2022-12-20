package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.VehicleRequestBodyDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private Set<VehicleReview> reviews;

    public Vehicle(VehicleRequestBodyDTO vehicleRequestBodyDTO, VehicleType vehicleType, Driver driver,
                   Location location) {
        this.model = vehicleRequestBodyDTO.getModel();
        this.registrationPlate = vehicleRequestBodyDTO.getLicenseNumber();
        this.seatNumber = vehicleRequestBodyDTO.getPassengerSeats();
        this.babyAllowed = vehicleRequestBodyDTO.isBabyTransport();
        this.petsAllowed = vehicleRequestBodyDTO.isPetTransport();
        this.vehicleType = vehicleType;
        this.driver = driver;
        this.location = location;
    }

    public Vehicle(String model, String registrationPlate, int seatNumber, boolean babyAllowed, boolean petsAllowed, Location location) {
        this.model = model;
        this.registrationPlate = registrationPlate;
        this.seatNumber = seatNumber;
        this.babyAllowed = babyAllowed;
        this.petsAllowed = petsAllowed;
        this.location = location;
    }

    public Vehicle(VehicleRequestBodyDTO vehicleRequest, VehicleType vehicleType) {
        this.model = vehicleRequest.getModel();
        this.registrationPlate = vehicleRequest.getLicenseNumber();
        this.seatNumber = vehicleRequest.getPassengerSeats();
        this.babyAllowed = vehicleRequest.isBabyTransport();
        this.petsAllowed = vehicleRequest.isPetTransport();
        this.location = new Location(vehicleRequest.getCurrentLocation());
        this.vehicleType = vehicleType;
    }

}
