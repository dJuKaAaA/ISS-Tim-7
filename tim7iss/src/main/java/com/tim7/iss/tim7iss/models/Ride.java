package com.tim7.iss.tim7iss.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

//    private VehicleType vehicleType;
//    private Driver driver;

//    private ArrayList<Message> messages;
//    private ArrayList<Review> reviews;
//    private ArrayList<Refusal> refusals;
//    private ArrayList<Passenger> passengers;
//    private ArrayList<Route> routes;

}
