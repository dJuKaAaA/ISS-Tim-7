package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.WorkHour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHourDto {

    private Long id;
    private String start;
    private String end;

    public WorkingHourDto(WorkHour workHour) {
        this.id = workHour.getId();
        this.start = workHour.getStartDate().toString();  // TODO: Change to better date format
        this.end = workHour.getEndDate().toString();  // TODO: Change to better date format
    }

}