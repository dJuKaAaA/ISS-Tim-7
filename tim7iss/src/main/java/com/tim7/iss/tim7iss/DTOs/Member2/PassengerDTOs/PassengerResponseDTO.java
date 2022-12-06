package com.tim7.iss.tim7iss.DTOs.Member2.PassengerDTOs;

import com.tim7.iss.tim7iss.models.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResponseDTO {
    private Long id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public PassengerResponseDTO(Passenger passenger) {
        this.id = passenger.getId();
        this.name = passenger.getFirstName();
        this.surname = passenger.getLastName();
        this.profilePicture = passenger.getProfilePicture();
        this.telephoneNumber = passenger.getPhoneNumber();
        this.email = passenger.getEmailAddress();
        this.address = passenger.getAddress();
    }

}