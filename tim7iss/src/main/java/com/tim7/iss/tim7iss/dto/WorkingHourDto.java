package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.WorkHour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHourDto {

    @Null(message = "Id should not be provided")
    private Long id;
    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d):([0-5]\\d)$",
            message = "Invalid date format")
    private String start;
    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d):([0-5]\\d)$",
            message = "Invalid date format")
    private String end;

    public WorkingHourDto(WorkHour workHour) {
        this.id = workHour.getId();
        this.start = workHour.getStartDate().format(Constants.customDateTimeFormat);
        this.end = workHour.getEndDate().format(Constants.customDateTimeFormat);
    }

}
