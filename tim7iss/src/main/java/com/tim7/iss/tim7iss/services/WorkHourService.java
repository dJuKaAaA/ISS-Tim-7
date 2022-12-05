package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.WorkHour;
import com.tim7.iss.tim7iss.repositories.WorkHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkHourService {

    @Autowired
    private WorkHourRepository workHourRepository;

    public void save(WorkHour workHour) {
        workHourRepository.save(workHour);
    }

    public WorkHour getById(Long id) {
        return workHourRepository.findById(id);
    }

}
