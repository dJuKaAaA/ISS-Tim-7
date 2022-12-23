package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String password;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getFirstName();
        this.surname = user.getLastName();
        this.profilePicture = user.getProfilePicture();
        this.telephoneNumber = user.getPhoneNumber();
        this.email = user.getEmailAddress();
        this.address = user.getAddress();
    }

}
