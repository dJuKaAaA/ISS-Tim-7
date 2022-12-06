package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getFirstName();
        this.surname = user.getLastName();
        this.profilePicture = user.getProfilePicture();
        this.telephoneNumber = user.getPhoneNumber();
        this.email = user.getEmailAddress();
        this.address = user.getAddress();
    }
}
