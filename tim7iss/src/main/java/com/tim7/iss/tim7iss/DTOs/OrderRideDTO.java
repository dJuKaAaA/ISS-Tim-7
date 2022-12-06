package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Enums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRideDTO {

    private Set<RouteDTO> locations;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

}
