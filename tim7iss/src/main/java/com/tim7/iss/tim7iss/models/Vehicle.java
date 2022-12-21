package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.VehicleRequestBodyDTO;
import com.tim7.iss.tim7iss.dto.VehicleDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String model;

    @NotBlank
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

    public Vehicle(VehicleDto vehicleRequestBodyDto, VehicleType vehicleType, Driver driver,
                   Location location) {
        this.model = vehicleRequestBodyDto.getModel();
        this.registrationPlate = vehicleRequestBodyDto.getLicenseNumber();
        this.seatNumber = vehicleRequestBodyDto.getPassengerSeats();
        this.babyAllowed = vehicleRequestBodyDto.getBabyTransport();
        this.petsAllowed = vehicleRequestBodyDto.getPetTransport();
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
