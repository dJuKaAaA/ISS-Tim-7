package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideEstimationsDto {

    private Integer estimatedTimeInMinutes;
    private Float estimatedCost;

}
