package com.tim7.iss.tim7iss.RequestDTOs;

import jakarta.persistence.Entity;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequestDTO {
    public String name,surname,profilePicture,telephoneNumber,email,address, password;
}
