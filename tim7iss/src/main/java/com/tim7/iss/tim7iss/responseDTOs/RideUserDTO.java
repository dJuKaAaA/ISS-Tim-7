package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideUserDTO {
    Long id;
    String email;
    String type;

    public RideUserDTO(User user, String type){
        if(user == null)
            return;
        this.id = user.getId();
        this.email = user.getEmailAddress();
        this.type = type;
    }
}
