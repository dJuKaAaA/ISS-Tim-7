package com.tim7.iss.tim7iss.DTOs.Member2.PanicDTOs;

import com.tim7.iss.tim7iss.DTOs.Member2.PassengerDTOs.PassengerResponseDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs.RideResponseDTO;
import com.tim7.iss.tim7iss.models.Panic;
import com.tim7.iss.tim7iss.models.Passenger;

public class PanicDTO {
    public Long id;
    public PassengerResponseDTO user;
    public RideResponseDTO ride;
    public String time;
    public String reason;

    public PanicDTO(Panic panic){
        this.id = panic.getId();
        this.user = new PassengerResponseDTO((Passenger) panic.getUser());
        this.ride = new RideResponseDTO(panic.getRide());
        this.time = String.valueOf(panic.getSentTime());
        this.reason = panic.getReason();
    }
}
