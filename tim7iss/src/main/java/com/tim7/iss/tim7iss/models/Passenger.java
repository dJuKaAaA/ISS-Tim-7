package com.tim7.iss.tim7iss.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Normalized;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger extends User {

    private String forDelete;  // TODO: Delete later

//    private Ride ongoingRide;
//    private ArrayList<Panic> panicList;
//    private ArrayList<Ride> finishedRides;
//    private ArrayList<Payment> payments;
//    private ArrayList<Route> favouriteRoutes;

}
