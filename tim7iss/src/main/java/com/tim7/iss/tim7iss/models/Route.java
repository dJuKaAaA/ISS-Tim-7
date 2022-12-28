package com.tim7.iss.tim7iss.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "Distance cannot be a negative number")
    private int distance;

    @NotNull(message = "Start point is mandatory")
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(name = "starting_point_id", referencedColumnName = "id")
    private Location startingPoint;

    @NotNull(message = "End point is mandatory")
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(name = "end_point_id", referencedColumnName = "id")
    private Location endPoint;

    public Route(Location startingPoint, Location endPoint) {
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
    }

}
