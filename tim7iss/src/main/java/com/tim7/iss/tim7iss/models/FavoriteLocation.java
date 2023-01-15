package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.FavoriteLocationDto;
import com.tim7.iss.tim7iss.dto.LocationForRideDto;
import com.tim7.iss.tim7iss.dto.RouteDto;
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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "favourite_routes",
            joinColumns = @JoinColumn(name = "favourite_location_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "route_id", referencedColumnName = "id")
    )
    private Set<Route> routes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "submited_by", referencedColumnName = "id")
    private Passenger submittedBy;

    @ManyToMany
    @JoinTable(
            name = "favorite_location_passengers",
            joinColumns = @JoinColumn(name = "favorite_location_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    )
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "vehicle_type", referencedColumnName = "id")
    private VehicleType vehicleType;

    private String favoriteName;
    private boolean babyTransport;
    private boolean petTransport;

    public FavoriteLocation(FavoriteLocationDto favoriteLocationDto, Set<Passenger> passengers, VehicleType vehicleType, Passenger passenger){
        this.submittedBy = passenger;
        this.favoriteName = favoriteLocationDto.getFavoriteName();
        for(RouteDto routeDto : favoriteLocationDto.getLocations()){
            float departureLatitude = routeDto.getDeparture().getLatitude();
            float departureLongitude = routeDto.getDeparture().getLongitude();
            float destinationLatitude = routeDto.getDestination().getLatitude();
            float destinationLongitude = routeDto.getDestination().getLongitude();
            Location departureLocation = new Location(routeDto.getDeparture().getAddress(), departureLongitude, departureLatitude);
            Location destinationLocation = new Location(routeDto.getDestination().getAddress(), destinationLongitude, destinationLatitude);
            this.routes.add(new Route(departureLocation, destinationLocation));
        }
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = favoriteLocationDto.isBabyTransport();
        this.petTransport = favoriteLocationDto.isPetTransport();
    }
}
