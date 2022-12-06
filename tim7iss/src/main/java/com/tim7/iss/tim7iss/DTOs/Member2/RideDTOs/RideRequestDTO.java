package com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs;

import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationRequestDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.PassengerDTOs.RideUserDTO;
import com.tim7.iss.tim7iss.models.*;

import java.util.List;
import java.util.Set;

public class RideRequestDTO {
    public Set<RideUserDTO> passengers;
    public Enums.VehicleName vehicleType;
    public Boolean babyTransport;
    public Boolean petTransport;
    public List<LocationRequestDTO> locations;
}
