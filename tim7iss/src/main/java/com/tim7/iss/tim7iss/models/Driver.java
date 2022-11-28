package com.tim7.iss.tim7iss.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User {

    private String document;
    private boolean isActive;
//    private Vehicle vehicle;
//    private ArrayList<WorkHours> workHours;
//    private ArrayList<Ride> rides;
//    private ArrayList<Panic> panicList;

}
