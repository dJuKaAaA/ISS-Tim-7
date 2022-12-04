package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.requestDTOs.LocationRequestDTO;
import com.tim7.iss.tim7iss.responseDTOs.LocationResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int distance;
    private int pricePerKm;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime estimatedTime;

    @OneToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "starting_point_id", referencedColumnName = "id")
    private Location startingPoint;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "end_point_id", referencedColumnName = "id")
    private List<Location> endPoints = new ArrayList<>();

    public Route(LocationResponseDTO startingPoint, List<LocationResponseDTO> locations){
        this.startingPoint = new Location(startingPoint);
        for(LocationResponseDTO locationResponseDTO : locations) {
            this.endPoints.add(new Location(locationResponseDTO));
        }
    }
}
