package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverChangeProfileRequestDto {

    @NotBlank(message = "Name must be provided")
    private String firstName;
    @NotBlank(message = "Surname must be provided")
    private String lastName;
    @NotBlank(message = "Profile picture must be provided")
    private String profilePicture;
    @Pattern(regexp = "^[0-9]{8,20}$", message = "Invalid format")
    private String phoneNumber;
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Address must be provided")
    private String address;

    // TODO staviti validaciju
    private String status;
    private boolean isMessageDisplayed;

    private Set<DriverChangeDocumentRequestDto> documents;


}


