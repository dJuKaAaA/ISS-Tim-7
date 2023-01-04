package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDto {

    @Pattern.List({
            @Pattern(regexp = "^(?=.*[0-9]).+", message = "Password must contain at least one number"),
            @Pattern(regexp = "^(?=.*[a-z]).+", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = "^(?=.*[A-Z]).+", message = "Password must contain at least one uppercase letter"),
            @Pattern(regexp = "^(?=.{8,20}).+", message = "Password must be between 8 and 20 characters")
    })
    public String newPassword;
    @Pattern.List({
            @Pattern(regexp = "^(?=.*[0-9]).+", message = "Password must contain at least one number"),
            @Pattern(regexp = "^(?=.*[a-z]).+", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = "^(?=.*[A-Z]).+", message = "Password must contain at least one uppercase letter"),
            @Pattern(regexp = "^(?=.{8,20}).+", message = "Password must be between 8 and 20 characters")
    })
    public String oldPassword;
}
