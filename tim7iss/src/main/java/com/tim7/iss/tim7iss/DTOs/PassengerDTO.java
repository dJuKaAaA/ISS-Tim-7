package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassengerDTO {
    Long id;
    String name,surname,profilePicture,telephoneNumber,email,address;

    public PassengerDTO(Passenger passenger) {
        this.id = passenger.getId();
        this.name = passenger.getFirstName();
        this.surname = passenger.getLastName();
        this.profilePicture = passenger.getProfilePicture();
        this.telephoneNumber = passenger.getPhoneNumber();
        this.email = passenger.getEmailAddress();
        this.address = passenger.getAddress();
    }

}
