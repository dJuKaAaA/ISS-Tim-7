package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleDriverDTO {
    private Long id;
    private String email;

    public SimpleDriverDTO(Driver driver){
        this.id = driver.getId();
        this.email = driver.getEmailAddress();
    }
}
