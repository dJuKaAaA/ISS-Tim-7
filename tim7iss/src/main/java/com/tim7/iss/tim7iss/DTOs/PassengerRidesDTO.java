package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassengerRidesDTO {
    Long id;
    String email;
    String type; // Always passenger???

    public PassengerRidesDTO(Passenger passenger){
        this.id = passenger.getId();
        this.email = passenger.getEmailAddress();
        this.type = "passenger";
    }
}
