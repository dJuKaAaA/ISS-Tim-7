package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.Panic;

public class PanicDTO {
    public Long id;
    public Long userId;
    public Long rideId;
    public String time;
    public String reason;

    public PanicDTO(Panic panic){
        this.id = panic.getId();
        this.userId = panic.getUser().getId();
        this.rideId = panic.getRide().getId();
        this.time = String.valueOf(panic.getTime());
        this.reason = panic.getReason();
    }

}
