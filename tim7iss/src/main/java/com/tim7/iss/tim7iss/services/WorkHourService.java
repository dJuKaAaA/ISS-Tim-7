package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.WorkHour;
import com.tim7.iss.tim7iss.repositories.WorkHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Streams.stream;

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

    public Long countAll() {
        return workHourRepository.count();
    }

    public int hoursWorked(Long driverId, LocalDate date) {
        List<WorkHour> workHours = getByDriverIdAndDate(driverId, date);
        int totalHoursWorked = 0;
        for (WorkHour workHour : workHours) {
            if (workHour.getEndDate() != null) {
                totalHoursWorked += workHour.getStartDate().until(workHour.getEndDate(), ChronoUnit.HOURS);
            } else {
                totalHoursWorked += workHour.getStartDate().until(LocalDateTime.now(), ChronoUnit.HOURS);
            }
        }
        return totalHoursWorked;

    }

    public Optional<WorkHour> getOngoingByDriverId(Long driverId) {
        return this.workHourRepository.findOngoingByDriverId(driverId);
    }

    public List<WorkHour> getByDriverIdAndDate(Long driverId, LocalDate date) {
        return this.workHourRepository.findByDriverIdBetweenDates(driverId, date, date.plusDays(1));
    }

}
