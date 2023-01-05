package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomTomSummaryDto   {
    private Integer lengthInMeters;
    private Integer travelTimeInSeconds;
    private Integer trafficDelayInSeconds;
    private Integer trafficLengthInMeters;
    private String departureTime;
    private String arrivalTime;
}
