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

    private Set<LocationDTO> locations;
    private Enums.VehicleName vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

}
