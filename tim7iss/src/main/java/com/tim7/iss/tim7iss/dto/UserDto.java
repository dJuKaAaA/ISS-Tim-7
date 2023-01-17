package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Surname cannot be blank")
    private String surname;
    private String profilePicture;
    @Pattern.List({
            @Pattern(regexp = "\\d+", message = "Phone number must contain only numbers"),
            @Pattern(regexp = "^(?=.{7,15}).+", message = "Phone number have between 7 and 15 digits")
    })
    private String telephoneNumber;
    @Email(message = "Invalid data. For example bad email format.")
    private String email;
    @NotBlank(message = "Address must be provided")
    private String address;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Pattern.List({
            @Pattern(regexp = "^(?=.*[0-9]).+", message = "Password must contain at least one number"),
            @Pattern(regexp = "^(?=.*[a-z]).+", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = "^(?=.*[A-Z]).+", message = "Password must contain at least one uppercase letter"),
            @Pattern(regexp = "^(?=.{8,20}).+", message = "Password must be between 8 and 20 characters")
    })
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
