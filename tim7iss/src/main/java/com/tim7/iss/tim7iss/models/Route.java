package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    private Long id;

    private int distance;
    private int pricePerKm;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime estimatedTime;

    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "starting_point_id", referencedColumnName = "id")
    private Location startingPoint;

    @ManyToOne
    @JoinColumn(name = "end_point_id", referencedColumnName = "id")
    private Location endPoint;

}
