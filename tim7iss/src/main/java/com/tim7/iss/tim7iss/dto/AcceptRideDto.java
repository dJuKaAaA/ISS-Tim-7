package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import com.tim7.iss.tim7iss.models.Constants;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptRideDto {

    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d):([0-5]\\d)$",
            message = "Invalid date format")
    private String time;

}
