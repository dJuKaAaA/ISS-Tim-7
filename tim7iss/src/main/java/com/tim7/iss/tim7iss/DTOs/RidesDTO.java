package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Ride;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RidesDTO {

    private int totalCount;
    private Set<RideDTO> results = new HashSet<>();

    public RidesDTO(Set<Ride> rides){
        for (Ride ride : rides){
            this.results.add(new RideDTO(ride));
        }
        this.totalCount = results.size();
    }
}
