package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteLocationDto {
    private String favoriteName;
    private Set<LocationsForRideDto> locations;
    private Set<UserRefDto> passengers;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
}
