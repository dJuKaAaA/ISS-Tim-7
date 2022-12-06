package com.tim7.iss.tim7iss.DTOs.Member2.PassengerDTOs;

import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RideUserDTO {
    Long id;
    String email;

    public RideUserDTO(User user){
        if(user == null)
            return;
        this.id = user.getId();
        this.email = user.getEmailAddress();
    }

    public RideUserDTO(){
        this.id = -1L;
        this.email = "";
    }
}
