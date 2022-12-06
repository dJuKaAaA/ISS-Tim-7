package com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class RideResponseListDTO {
    public Integer totalCount;
    public Set<RideResponseDTO> results;
    public RideResponseListDTO(){
        this.totalCount = 0;
        this.results = new HashSet<>();
    }
}
