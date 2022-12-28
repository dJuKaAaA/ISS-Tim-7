package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverChangeProfileRequestDto {

    private String firstName;
    private String lastName;
    private String profilePicture;
    private String phoneNumber;
    private String email;
    private String address;
    private String status;
    private boolean isMessageDisplayed;

    private Set<DriverChangeDocumentRequestDto> documents;


}


