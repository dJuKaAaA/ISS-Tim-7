package com.tim7.iss.tim7iss.responseDTOs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PassengerResponseListDTO {
    public int totalCount;
    public List<PassengerResponseDTO>results;
    public PassengerResponseListDTO(){
        this.totalCount = 0;
        this.results = new ArrayList<>();
    }
}
