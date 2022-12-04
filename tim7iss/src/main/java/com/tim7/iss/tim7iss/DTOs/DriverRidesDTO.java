package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DriverRidesDTO {
    private Long id;
    private String email;
    private String type; // Always driver???

    public DriverRidesDTO(Driver driver){
        this.id = driver.getId();
        this.email = driver.getEmailAddress();
        this.type = "Driver";
    }
}
