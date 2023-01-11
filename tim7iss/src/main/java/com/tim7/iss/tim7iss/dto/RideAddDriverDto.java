package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideAddDriverDto {
    @NotBlank(message = "Ride id must be provided")
    private long rideId;
    @NotBlank(message = "Driver id must be provided")
    private long driverId;
}
