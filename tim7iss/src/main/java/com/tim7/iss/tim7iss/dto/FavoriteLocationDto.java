package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.FavoriteLocation;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteLocationDto {
    private Long id;
    private String favoriteName;
    private Set<RouteDto> locations = new HashSet<>();
    private Set<UserRefDto> passengers = new HashSet<>();
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public FavoriteLocationDto(FavoriteLocation favoriteLocation){
        this.id = favoriteLocation.getId();
        this.favoriteName = favoriteLocation.getFavoriteName();
        for(Route route : favoriteLocation.getRoutes()){
            this.locations.add(new RouteDto(route));
        }
        for(Passenger passenger : favoriteLocation.getPassengers()){
            this.passengers.add(new UserRefDto(passenger));
        }
        this.vehicleType = favoriteLocation.getVehicleType().getName();
        this.babyTransport = favoriteLocation.isBabyTransport();
        this.petTransport = favoriteLocation.isPetTransport();
    }
}
