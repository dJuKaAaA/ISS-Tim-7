package com.tim7.iss.tim7iss.DTOs.apidriver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedDriverWorkHoursResponseDTO {

    private long totalCount;
    private Collection<WorkHourResponseDTO> results;

}
