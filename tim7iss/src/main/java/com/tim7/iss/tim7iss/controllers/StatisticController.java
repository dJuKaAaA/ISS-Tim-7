package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import com.tim7.iss.tim7iss.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/number/finished/rides/{userId}")
    public ResponseEntity<StatisticReportDto> getTotalNumberOfAcceptedRides(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Integer numberOfAcceptedRides = statisticService.getTotalNumberOfFinishedRides(staDate, endDate, userId);
        StatisticReportDto statisticReportDto = new StatisticReportDto(numberOfAcceptedRides);
        return new ResponseEntity<>(statisticReportDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/finished/rides/{userId}")
    public ResponseEntity<RidesReportDto> getAcceptedRides(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        List<RideDto> acceptedRides = statisticService.getFinishedRides(staDate, endDate, userId);
        RidesReportDto ridesReportDto = new RidesReportDto(acceptedRides);
        return new ResponseEntity<>(ridesReportDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/number/rejected/rides/{userId}")
    public ResponseEntity<StatisticReportDto> getTotalNumberOfRejectedRides(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Integer numberOfRejectedRides = statisticService.getTotalNumberOfRejectedRides(staDate, endDate, userId);
        StatisticReportDto statisticReportDto = new StatisticReportDto(numberOfRejectedRides);
        return new ResponseEntity<>(statisticReportDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/rejected/rides/{userId}")
    public ResponseEntity<RidesReportDto> getRejectedRides(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        List<RideDto> acceptedRides = statisticService.getRejectedRides(staDate, endDate, userId);
        RidesReportDto ridesReportDto = new RidesReportDto(acceptedRides);
        return new ResponseEntity<>(ridesReportDto, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/workHours/{driverId}")
    public ResponseEntity<StatisticReportDto> getTotalNumberOfWorkHours(@PathVariable("driverId") Long driverId, @RequestBody CreateReportDto createReportDto) {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Integer workHours = statisticService.getTotalNumberOfWorkHours(staDate, endDate, driverId);
        StatisticReportDto statisticReportDto = new StatisticReportDto(workHours);
        return new ResponseEntity<>(statisticReportDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/profit/{driverId}")
    public ResponseEntity<StatisticReportDto> getProfit(@PathVariable("driverId") Long driverId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime staDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Integer profit = statisticService.getProfit(staDate, endDate, driverId);
        StatisticReportDto statisticReportDto = new StatisticReportDto(profit);
        return new ResponseEntity<>(statisticReportDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/numberOfRidesPerDay/{userId}")
    public ResponseEntity<List<DateReport>> getNumberOfRidesPerDay(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> numberOfRidesPerDay = statisticService.getNumberOfRidesPerDay(startDate, endDate, userId);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : numberOfRidesPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }
        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/traveledDistancePerDay/{userId}")
    public ResponseEntity<List<DateReport>> getTraveledDistancePerDay(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> traveledDistancePerDay = statisticService.getTraveledDistancePerDay(startDate, endDate, userId);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : traveledDistancePerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER') or hasRole('DRIVER')")
    @PostMapping("/api/statistic/financialsPerDay/{userId}")
    public ResponseEntity<List<DateReport>> getFinancialsPerDay(@PathVariable("userId") Long userId, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> financialsPerDay = statisticService.getFinancialsPerDay(startDate, endDate, userId);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : financialsPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/numberOfRidesPerDayByEmail/{email}")
    public ResponseEntity<List<DateReport>> getNumberOfRidesPerDayByEmail(@PathVariable("email") String email, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> numberOfRidesPerDay = statisticService.getNumberOfRidesPerDayByEmail(startDate, endDate, email);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : numberOfRidesPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }
        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/traveledDistancePerDayByEmail/{email}")
    public ResponseEntity<List<DateReport>> getTraveledDistancePerDayByEmail(@PathVariable("email") String email, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> traveledDistancePerDay = statisticService.getTraveledDistancePerDayByEmail(startDate, endDate, email);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : traveledDistancePerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/financialsPerDayByEmail/{email}")
    public ResponseEntity<List<DateReport>> getFinancialsPerDayByEmail(@PathVariable("email") String email, @RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> financialsPerDay = statisticService.getFinancialsPerDayByEmail(startDate, endDate, email);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : financialsPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/numberOfRidesPerDay/drivers")
    public ResponseEntity<List<DateReport>> getNumberOfRidesPerDayAllDrivers(@RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> numberOfRidesPerDay = statisticService.getNumberOfRidesPerDayAllDrivers(startDate, endDate);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : numberOfRidesPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }
        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/traveledDistancePerDay/drivers")
    public ResponseEntity<List<DateReport>> getTraveledDistancePerDayAllDrivers(@RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> traveledDistancePerDay = statisticService.getTraveledDistancePerDayAllDrivers(startDate, endDate);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : traveledDistancePerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/financialsPerDay/drivers")
    public ResponseEntity<List<DateReport>> getFinancialsPerDayAllDrivers(@RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> financialsPerDay = statisticService.getFinancialsPerDayAllDrivers(startDate, endDate);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : financialsPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/numberOfRidesPerDay/passengers")
    public ResponseEntity<List<DateReport>> getNumberOfRidesPerDayAllPassengers(@RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> numberOfRidesPerDay = statisticService.getNumberOfRidesPerDayAllPassengers(startDate, endDate);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : numberOfRidesPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }
        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/traveledDistancePerDay/passengers")
    public ResponseEntity<List<DateReport>> getTraveledDistancePerDayAllPassengers(@RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> traveledDistancePerDay = statisticService.getTraveledDistancePerDayAllPassenger(startDate, endDate);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : traveledDistancePerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/statistic/financialsPerDay/passengers")
    public ResponseEntity<List<DateReport>> getFinancialsPerDayAllPassengers(@RequestBody CreateReportDto createReportDto) throws UserNotFoundException {
        LocalDateTime startDate = LocalDateTime.parse(createReportDto.getStartDate(), Constants.customDateTimeFormat);
        LocalDateTime endDate = LocalDateTime.parse(createReportDto.getEndDate(), Constants.customDateTimeFormat);
        Map<LocalDate, Integer> financialsPerDay = statisticService.getFinancialsPerDayAllPassengers(startDate, endDate);

        List<DateReport> dateReports = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : financialsPerDay.entrySet()) {
            DateReport dateReport = new DateReport(entry.getKey().atStartOfDay().format(Constants.customDateTimeFormat), entry.getValue());
            dateReports.add(dateReport);
        }

        dateReports = statisticService.sortDateReportsByDate(dateReports);
        return new ResponseEntity<>(dateReports, HttpStatus.OK);
    }
}
