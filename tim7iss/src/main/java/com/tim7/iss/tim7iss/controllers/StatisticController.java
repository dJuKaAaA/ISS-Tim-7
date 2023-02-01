package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Transactional
@CrossOrigin
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    @PostMapping("/api/statistic/number/finished/rides/{userId}")
    public ResponseEntity<StatisticReportDto> getTotalNumberOfAcceptedRides(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Integer numberOfAcceptedRides = statisticService.getTotalNumberOfFinishedRides(staDate, endDate, userId);
        StatisticReportDto statisticReportDto = new StatisticReportDto(numberOfAcceptedRides);
        return new ResponseEntity<>(statisticReportDto, HttpStatus.OK);
    }

    @PostMapping("/api/statistic/finished/rides/{userId}")
    public ResponseEntity<RidesReportDto> getAcceptedRides(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        List<RideDto> acceptedRides = statisticService.getFinishedRides(staDate, endDate, userId);
        RidesReportDto ridesReportDto = new RidesReportDto(acceptedRides);
        return new ResponseEntity<>(ridesReportDto, HttpStatus.OK);
    }

    @PostMapping("/api/statistic/number/rejected/rides/{userId}")
    public ResponseEntity<StatisticReportDto> getTotalNumberOfRejectedRides(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Integer numberOfRejectedRides = statisticService.getTotalNumberOfRejectedRides(staDate, endDate, userId);
        StatisticReportDto statisticReportDto = new StatisticReportDto(numberOfRejectedRides);
        return new ResponseEntity<>(statisticReportDto, HttpStatus.OK);
    }

    @PostMapping("/api/statistic/rejected/rides/{userId}")
    public ResponseEntity<RidesReportDto> getRejectedRides(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        List<RideDto> acceptedRides = statisticService.getRejectedRides(staDate, endDate, userId);
        RidesReportDto ridesReportDto = new RidesReportDto(acceptedRides);
        return new ResponseEntity<>(ridesReportDto, HttpStatus.OK);
    }


    @PostMapping("/api/statistic/workHours/{driverId}")
    public ResponseEntity<StatisticReportDto> getTotalNumberOfWorkHours(@PathVariable("driverId") Long driverId, @RequestBody CreateReportDto createReportDto) {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Integer workHours = statisticService.getTotalNumberOfWorkHours(staDate, endDate, driverId);
        StatisticReportDto statisticReportDto = new StatisticReportDto(workHours);
        return new ResponseEntity<>(statisticReportDto, HttpStatus.OK);
    }

    @PostMapping("/api/statistic/profit/{driverId}")
    public ResponseEntity<StatisticReportDto> getProfit(@PathVariable("driverId") Long driverId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Integer profit = statisticService.getProfit(staDate, endDate, driverId);
        StatisticReportDto statisticReportDto = new StatisticReportDto(profit);
        return new ResponseEntity<>(statisticReportDto, HttpStatus.OK);
    }

    @PostMapping("/api/statistic/numberOfRidesPerDay/{userId}")
    public ResponseEntity<List<DateReport>> getNumberOfRidesPerDay(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> numberOfRidesPerDay = statisticService.getNumberOfRidesPerDay(staDate, endDate, userId);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : numberOfRidesPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }
        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PostMapping("/api/statistic/traveledDistancePerDay/{userId}")
    public ResponseEntity<List<DateReport>> getTraveledDistancePerDay(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> traveledDistancePerDay = statisticService.getTraveledDistancePerDay(staDate, endDate, userId);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : traveledDistancePerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PostMapping("/api/statistic/financialsPerDay/{userId}")
    public ResponseEntity<List<DateReport>> getFinancialsPerDay(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> traveledDistancePerDay = statisticService.getFinancialsPerDay(startDate, endDate, userId);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : traveledDistancePerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }
}
