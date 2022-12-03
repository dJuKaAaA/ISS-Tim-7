package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.Passenger;
import lombok.Data;

@Data
public class PassengerResponseDTO {
    public Long id;
    public String name,surname,profilePicture,telephoneNumber,email,adress;

    public PassengerResponseDTO(Passenger passenger) {
        this.id = passenger.getId();
        this.name = passenger.getFirstName();
        this.surname = passenger.getLastName();
        this.profilePicture = passenger.getProfilePicture();
        this.telephoneNumber = passenger.getPhoneNumber();
        this.email = passenger.getEmailAddress();
        this.adress = passenger.getAddress();
    }

}
