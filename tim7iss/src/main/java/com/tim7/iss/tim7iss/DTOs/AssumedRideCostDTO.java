package com.tim7.iss.tim7iss.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssumedRideCostDTO {
    private int estimatedTimeInMinutes;
    private double estimatedCost;
}
