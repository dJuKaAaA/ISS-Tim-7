package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimplePassengerDTO {
    Long id;
    String email;

    public SimplePassengerDTO(Passenger passenger){
        this.id = passenger.getId();
        this.email = passenger.getEmailAddress();
    }
}
