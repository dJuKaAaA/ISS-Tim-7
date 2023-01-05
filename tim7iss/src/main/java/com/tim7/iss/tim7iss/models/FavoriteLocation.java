package com.tim7.iss.tim7iss.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "favourite_routes",
            joinColumns = @JoinColumn(name = "favourite_location_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "route_id", referencedColumnName = "id")
    )
    private Set<Route> routes = new HashSet<>();

    @ManyToMany(mappedBy = "favouriteLocations")
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "vehicle_type", referencedColumnName = "id")
    private VehicleType vehicleType;

    private boolean babyTransport;
    private boolean petTransport;
}
