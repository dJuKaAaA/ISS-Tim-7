package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.Ride;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
public class RidesFilterResponseListDTO {
    public Integer totalCount;
    public Set<RidesFilterResponseDTO> results;
    public RidesFilterResponseListDTO(){
        this.totalCount = 0;
        this.results = new HashSet<>();
    }
}
