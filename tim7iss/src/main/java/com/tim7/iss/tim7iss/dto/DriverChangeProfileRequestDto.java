package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.DriverDocumentChangeRequest;
import com.tim7.iss.tim7iss.models.DriverProfileChangeRequest;
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

    @Pattern(regexp="^(PENDING|ACCEPTED|REJECTED)$",
            message = "Invalid status... Must be PENDING, ACCEPTED, REJECTED")
    private String status;
    private boolean isMessageDisplayed;

    private Set<DriverChangeDocumentRequestDto> documents;


    public DriverChangeProfileRequestDto(DriverProfileChangeRequest request) {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.profilePicture = request.getProfilePicture();
        this.phoneNumber = request.getPhoneNumber();
        this.email = request.getEmail();
        this.address = request.getAddress();
        this.status = request.getStatus();
        this.isMessageDisplayed = request.isMessageDisplayed();
        this.documents = new HashSet<>();
        for(DriverDocumentChangeRequest documentRequest: request.getDriverDocumentChangeRequests()){
            documents.add(new DriverChangeDocumentRequestDto(documentRequest));
        }
    }
}


