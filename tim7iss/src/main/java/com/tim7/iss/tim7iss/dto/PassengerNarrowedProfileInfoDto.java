package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerNarrowedProfileInfoDto {

    private String email;
    private String profilePicture;
    private String fullName;

    public PassengerNarrowedProfileInfoDto(Passenger passenger) {
        this.email = passenger.getEmailAddress();
        this.profilePicture = new String(passenger.getProfilePicture());
        this.fullName = passenger.getFirstName() + " " + passenger.getLastName();
    }

}
