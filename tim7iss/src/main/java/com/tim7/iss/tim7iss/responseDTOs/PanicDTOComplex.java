package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.Panic;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.User;

public class PanicDTOComplex {
    public Long id;
    public PassengerResponseDTO user;
    public RideResponseDTO ride;
    public String time;
    public String reason;

    public PanicDTOComplex(Panic panic){
        this.id = panic.getId();
        this.user = new PassengerResponseDTO((Passenger) panic.getUser());
        this.ride = new RideResponseDTO(panic.getRide());
        this.time = String.valueOf(panic.getTime());
        this.reason = panic.getReason();
    }
}
