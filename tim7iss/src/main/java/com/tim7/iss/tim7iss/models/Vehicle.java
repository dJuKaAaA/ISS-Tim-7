package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.VehicleRequestBodyDTO;
import com.tim7.iss.tim7iss.repositories.VehicleTypeRepository;
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

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

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
}
