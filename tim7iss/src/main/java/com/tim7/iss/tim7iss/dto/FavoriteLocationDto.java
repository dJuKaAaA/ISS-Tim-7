package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.FavoriteLocation;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteLocationDto {
    private Long id;
    @NotEmpty
    private String favoriteName;
    @NotEmpty
    private Set<LocationForRideDto> locations = new HashSet<>();
    @NotEmpty
    private Set<UserRefDto> passengers = new HashSet<>();
    @NotNull
    @Pattern(regexp = "^STANDARD|LUXURY|VAN$", message = "Invalid vehicle type... Must be STANDARD, LUXURY or VAN")
    private String vehicleType;
    @NotNull(message = "Baby transport must be provided")
    private boolean babyTransport;
    @NotNull(message = "Pet transport must be provided")
    private boolean petTransport;

    private String scheduledTime;

    public FavoriteLocationDto(FavoriteLocation favoriteLocation){
        this.id = favoriteLocation.getId();
        this.favoriteName = favoriteLocation.getFavoriteName();
        for(Route route : favoriteLocation.getRoutes()){
            this.locations.add(new LocationForRideDto(route));
        }
        for(Passenger passenger : favoriteLocation.getPassengers()){
            this.passengers.add(new UserRefDto(passenger));
        }
        this.vehicleType = favoriteLocation.getVehicleType().getName();
        this.babyTransport = favoriteLocation.isBabyTransport();
        this.petTransport = favoriteLocation.isPetTransport();
    }
}
