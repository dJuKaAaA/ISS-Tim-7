package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnregisteredUserRideCreation {

    @NotEmpty(message = "There must be at least one route for the driver to travel")
    private List<LocationForRideDto> locations = new ArrayList<>();
    @Pattern(regexp = "^STANDARD|LUXURY|VAN$", message = "Invalid vehicle type... Must be STANDARD, LUXURY or VAN")
    private String vehicleType;
    @NotNull(message = "Baby transport must be provided")
    private Boolean babyTransport;
    @NotNull(message = "Pet transport must be provided")
    private Boolean petTransport;
}
