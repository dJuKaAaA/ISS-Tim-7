package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanicCreateDto {

    @NotBlank(message = "Reason must be provided (cannot be empty)")
    private String reason;

}
