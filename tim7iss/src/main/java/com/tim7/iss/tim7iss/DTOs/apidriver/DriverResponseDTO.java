package com.tim7.iss.tim7iss.DTOs.apidriver;

import com.tim7.iss.tim7iss.models.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Decide whether to replace DriverDTO and PassengerDTO with just UserDTO

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponseDTO {

    private Long id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public DriverResponseDTO(Driver driver) {
        this.id = driver.getId();
        this.name = driver.getFirstName();
        this.surname = driver.getLastName();
        this.profilePicture = driver.getProfilePicture();
        this.telephoneNumber = driver.getPhoneNumber();
        this.email = driver.getEmailAddress();
        this.address = driver.getAddress();
    }

    public DriverResponseDTO(String name, String surname, String profilePicture, String telephoneNumber, String email, String address) {
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
    }


}
