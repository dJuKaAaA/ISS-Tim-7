package com.tim7.iss.tim7iss.DTOs.apidriver;

import com.tim7.iss.tim7iss.models.Ride;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRidesResponseDTO {

    private long totalCount;
    private Collection<RideResponseDTO> results;

}
