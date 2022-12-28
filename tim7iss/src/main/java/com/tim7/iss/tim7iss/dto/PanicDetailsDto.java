package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Panic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tim7.iss.tim7iss.models.Constants;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanicDetailsDto {

    private Long id;
    private UserDto user;
    private RideDto ride;
    private String time;
    private String reason;

    public PanicDetailsDto(Panic panic) {
        this.id = panic.getId();
        this.user = new UserDto(panic.getUser());
        this.ride = new RideDto(panic.getRide());
        this.time = panic.getSentTime().format(Constants.customDateTimeFormat);
        this.reason = panic.getReason();
    }

}
