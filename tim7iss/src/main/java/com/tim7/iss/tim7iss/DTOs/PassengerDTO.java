package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Passenger;

public class PassengerDTO {
    Long id;
    String name,surname,profilePicture,telephoneNumber,email,adress;

    public PassengerDTO(Passenger passenger) {
        this.id = passenger.getId();
        this.name = passenger.getFirstName();
        this.surname = passenger.getLastName();
        this.profilePicture = passenger.getProfilePicture();
        this.telephoneNumber = passenger.getPhoneNumber();
        this.email = passenger.getEmailAddress();
        this.adress = passenger.getAddress();
    }

}
