package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.DriverProfileChangeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverChangeProfileDriverRequestDto {

    private String firstName;
    private String lastName;
    private String profilePicture;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private Set<DriverChangeDocumentRequestDto> documents = new HashSet();


}
