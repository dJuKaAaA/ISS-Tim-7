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
public class RideCreationDto {

    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d):([0-5]\\d)$",
            message = "Invalid date format")
    private String scheduledTime;
    @NotEmpty(message = "There must be at least one route for the driver to travel")
    private List<LocationForRideDto> locations = new ArrayList<>();
    @NotEmpty(message = "There must be at least one passenger board")
    private List<UserRefDto> passengers = new ArrayList<>();
    @Pattern(regexp = "^STANDARD|LUXURY|VAN$", message = "Invalid vehicle type... Must be STANDARD, LUXURY or VAN")
    private String vehicleType;
    @NotNull(message = "Baby transport must be provided")
    private Boolean babyTransport;
    @NotNull(message = "Pet transport must be provided")
    private Boolean petTransport;

}
