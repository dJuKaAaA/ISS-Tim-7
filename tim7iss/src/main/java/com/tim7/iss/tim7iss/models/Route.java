package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Make not negative constraint
    private int distance;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "starting_point_id", referencedColumnName = "id")
    private Location startingPoint;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "end_point_id", referencedColumnName = "id")
    private Location endPoint;

    public Route(Location startingPoint, Location endPoint) {
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
    }

}
