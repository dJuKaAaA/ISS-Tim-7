package com.tim7.iss.tim7iss.responses;

import com.tim7.iss.tim7iss.DTOs.WorkHourDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverWorkHoursResponse {

    private long totalCount;
    private Collection<WorkHourDTO> results;

}
