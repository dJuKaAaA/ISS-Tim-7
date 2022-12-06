package com.tim7.iss.tim7iss.DTOs.Member2.PanicDTOs;

import com.tim7.iss.tim7iss.models.Panic;

import java.util.ArrayList;
import java.util.List;

public class PanicDTOList {
    public Integer totalCount;
    public List<PanicDTO> results;

    public PanicDTOList(){
        totalCount = 0;
        results = new ArrayList<>();
    }

    public void addPanic(Panic panic) {
        this.results.add(new PanicDTO(panic));
        this.totalCount += 1;
    }
}
