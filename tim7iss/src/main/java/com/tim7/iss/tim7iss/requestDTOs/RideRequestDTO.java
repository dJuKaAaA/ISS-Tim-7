package com.tim7.iss.tim7iss.requestDTOs;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.responseDTOs.RideUserDTO;

import java.util.List;
import java.util.Set;

public class RideRequestDTO {
    public Set<RideUserDTO> passengers;
    public Enums.VehicleName vehicleType;
    public Boolean babyTransport;
    public Boolean petTransport;
    public List<LocationRequestDTO> locations;
}
