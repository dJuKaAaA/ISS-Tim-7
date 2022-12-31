package com.tim7.iss.tim7iss.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distance;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "starting_point_id", referencedColumnName = "id")
    private Location startingPoint;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "end_point_id", referencedColumnName = "id")
    private Location endPoint;

    public Route(Location startingPoint, Location endPoint) {
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
    }

//    public Route(Route route) {
//        this.setId(null);
//        this.setDistance(route.getDistance());
//        this.setStartingPoint(new Location(route.getStartingPoint()));
//        this.setEndPoint(new Location(route.getEndPoint());
//    }

    @Override
    public Route clone() {
        try {
            Route clone = (Route) super.clone();
            clone.setId(null);
            clone.setDistance(this.distance);
            clone.setStartingPoint(this.startingPoint.clone());
            clone.setEndPoint(this.endPoint.clone());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
