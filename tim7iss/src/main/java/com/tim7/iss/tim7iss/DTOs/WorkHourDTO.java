package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.WorkHour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkHourDTO {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    public WorkHourDTO(WorkHour workHours) {
        this.id = workHours.getId();
        this.start = workHours.getStartDate();
        this.end = workHours.getEndDate();
    }

}
