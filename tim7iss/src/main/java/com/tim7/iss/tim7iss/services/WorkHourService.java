package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.WorkHour;
import com.tim7.iss.tim7iss.repositories.WorkHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class WorkHourService {

    @Autowired
    private WorkHourRepository workHourRepository;

    public void save(WorkHour workHour) {
        workHourRepository.save(workHour);
    }

    public Optional<WorkHour> getById(Long id) {
        return workHourRepository.findById(id);
    }

    public Collection<WorkHour> getByDriverId(long driverId, Pageable page) {
        return workHourRepository.findByDriverId(driverId, page);
    }

    public List<WorkHour> getBetweenStartDateAndEndDateByDriverId(Long driverId, LocalDateTime date) {
        return workHourRepository.findBetweenStartDateAndEndDateByDriverId(driverId, date);
    }

    public Long countAll() {
        return workHourRepository.count();
    }

}
