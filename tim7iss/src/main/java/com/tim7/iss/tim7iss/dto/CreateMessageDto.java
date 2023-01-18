package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMessageDto {

    @NotBlank(message = "No content for message provided")
    private String message;
    @Pattern(regexp="^(SUPPORT|RIDE|PANIC)$", message = "Invalid message type... Must be SUPPORT, RIDE or PANIC")
    private String type;
    private Long rideId;
}
