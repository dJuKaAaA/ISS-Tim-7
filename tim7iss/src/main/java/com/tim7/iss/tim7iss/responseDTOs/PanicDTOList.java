package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.Panic;

import java.util.ArrayList;
import java.util.List;

public class PanicDTOList {
    public Integer totalCount;
    public List<PanicDTOComplex> results;

    public PanicDTOList(){
        totalCount = 0;
        results = new ArrayList<>();
    }

    public void addPanic(Panic panic) {
        this.results.add(new PanicDTOComplex(panic));
        this.totalCount += 1;
    }
}
