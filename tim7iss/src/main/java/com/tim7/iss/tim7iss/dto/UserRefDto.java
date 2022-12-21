package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRefDto {

    private Long id;
    private String email;

    public UserRefDto(User user) {
        this.id = user.getId();
        this.email = user.getEmailAddress();
    }

}
