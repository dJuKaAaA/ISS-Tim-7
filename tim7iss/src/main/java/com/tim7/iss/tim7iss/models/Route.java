package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @Min(value = 0, message = "Distance cannot be a negative number")
    private int distance;

    // cascade = CascadeType.PERSIST is not recommended
    // before instantiating a route make sure the locations exist in the database
    // if not, add them using locationService
    // or use persist but still check if locations exist because an exception will be thrown
    // a unique constraint exception for latitude and longitude
    @NotNull(message = "Start point is mandatory")
    @ManyToOne
    @JoinColumn(name = "starting_point_id", referencedColumnName = "id")
    private Location startingPoint;

    @NotNull(message = "End point is mandatory")
    @ManyToOne
    @JoinColumn(name = "end_point_id", referencedColumnName = "id")
    private Location endPoint;

    public Route(Location startingPoint, Location endPoint) {
        this.startingPoint = startingPoint;
        this.endPoint = endPoint;
    }

}
