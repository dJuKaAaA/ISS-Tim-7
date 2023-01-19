package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.EndShiftDto;
import com.tim7.iss.tim7iss.dto.StartShiftDto;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.WorkHour;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.WorkHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Streams.stream;

@Service
public class WorkHourService {

    @Autowired
    private WorkHourRepository workHourRepository;
    @Autowired
    private DriverRepository driverRepository;

    public void save(WorkHour workHour) {
        workHourRepository.save(workHour);
    }

    public WorkHour getById(Long id) throws WorkHourNotFoundException {
        return workHourRepository.findById(id).orElseThrow(WorkHourNotFoundException::new);
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

    public WorkHour startShiftForDriver(StartShiftDto startShiftDto, Long driverId)
            throws VehicleNotAssignedException, ShiftAlreadyOngoingException, ShiftExceededException,
            DriverNotFoundException {

        Driver driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);
        if (driver.getVehicle() == null) {
            throw new VehicleNotAssignedException("Cannot start shift because the vehicle is not defined!");
        }
        if (workHourRepository.findOngoingByDriverId(driverId).isPresent()) {
            throw new ShiftAlreadyOngoingException();
        }
        if (hoursWorked(driverId, LocalDate.now()) >= 8) {
            throw new ShiftExceededException();
        }

        WorkHour newWorkHour = new WorkHour();
        newWorkHour.setStartDate(LocalDateTime.parse(startShiftDto.getStart(), Constants.customDateTimeFormat));
        newWorkHour.setDriver(driver);
        return workHourRepository.save(newWorkHour);
    }

    public WorkHour endShiftForDriver(EndShiftDto endShiftDto, String driverEmail)
            throws VehicleNotAssignedException, DriverNotFoundException, NoShiftOngoingException {
        Driver driver = driverRepository.findByEmailAddress(driverEmail).orElseThrow(DriverNotFoundException::new);
        if (driver.getVehicle() == null) {
            throw new VehicleNotAssignedException();
        }
        WorkHour workHour = workHourRepository.findOngoingByDriverId(driver.getId()).
                orElseThrow(NoShiftOngoingException::new);
        workHour.setEndDate(LocalDateTime.parse(endShiftDto.getEnd(), Constants.customDateTimeFormat));
        return workHourRepository.save(workHour);
    }

    public WorkHour endShiftForDriver(EndShiftDto endShiftDto, Long driverId)
            throws DriverNotFoundException, VehicleNotAssignedException, NoShiftOngoingException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);
        if (driver.getVehicle() == null) {
            throw new VehicleNotAssignedException();
        }
        WorkHour workHour = workHourRepository.findOngoingByDriverId(driver.getId()).
                orElseThrow(NoShiftOngoingException::new);
        workHour.setEndDate(LocalDateTime.parse(endShiftDto.getEnd(), Constants.customDateTimeFormat));
        return workHourRepository.save(workHour);
    }

}
