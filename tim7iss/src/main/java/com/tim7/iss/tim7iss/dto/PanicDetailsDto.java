package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanicDetailsDto {

    private Long id;
    private UserDto user;
    private RideDto ride;
    private String time;
    private String reason;

}
