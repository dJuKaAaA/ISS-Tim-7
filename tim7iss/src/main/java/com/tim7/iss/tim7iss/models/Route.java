package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int distance;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "starting_point_id", referencedColumnName = "id")
    private Location startingPoint;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "end_point_id", referencedColumnName = "id")
    private Set<Location> endPoints = new HashSet<>();

}
