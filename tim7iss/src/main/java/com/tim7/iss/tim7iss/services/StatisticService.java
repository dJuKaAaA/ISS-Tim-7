package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.LocationForRideDto;
import com.tim7.iss.tim7iss.dto.RideDto;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class StatisticService {

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StatisticService.class);

    @Autowired
    RideRepository rideRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RideService rideService;
    @Autowired
    WorkHourService workHourService;
    @Autowired
    MapService mapService;

    public Integer getTotalNumberOfFinishedRides(LocalDateTime startDate, LocalDateTime endDate, Long userId) throws UserNotFoundException {

        List<RideDto> rides = rideService.getAllFinishedRides(userId);
        Integer numberOfAcceptedRides = 0;
        for (RideDto ride : rides) {
            LocalDateTime startRideTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            if ((startRideTime.isAfter(startDate) || startRideTime.equals(startDate)) && (startRideTime.isBefore(endDate) || startRideTime.equals(endDate))) {
                numberOfAcceptedRides++;
            }
        }
        return numberOfAcceptedRides;
    }

    public Integer getTotalNumberOfRejectedRides(LocalDateTime startDate, LocalDateTime endDate, Long userId) throws UserNotFoundException {

        List<RideDto> rides = rideService.getAllRejectedRides(userId);
        Integer numberOfRejectedRides = 0;
        for (RideDto ride : rides) {
            LocalDateTime startRideTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            if ((startRideTime.isAfter(startDate) || startRideTime.equals(startDate)) && (startRideTime.isBefore(endDate) || startRideTime.equals(endDate))) {
                numberOfRejectedRides++;
            }
        }
        return numberOfRejectedRides;
    }

    public Integer getTotalNumberOfCanceledRides(LocalDateTime startDate, LocalDateTime endDate, Long userId) throws UserNotFoundException {
        List<RideDto> rides = getAllCanceledRides(userId);
        Integer numberOfCanceledRides = 0;
        for (RideDto ride : rides) {
            LocalDateTime startRideTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            if ((startRideTime.isAfter(startDate) || startRideTime.equals(startDate)) && (startRideTime.isBefore(endDate) || startRideTime.equals(endDate))) {
                numberOfCanceledRides++;
            }
        }
        return numberOfCanceledRides;
    }

    public Integer getTotalNumberOfWorkHours(LocalDateTime startDate, LocalDateTime endDate, Long driverId) {
        Integer workHours = 0;
        while (startDate.isBefore(endDate)) {
            workHours = workHours + workHourService.hoursWorked(driverId, LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth()));
            startDate = startDate.plusDays(1);
        }
        workHours = workHourService.hoursWorked(driverId, LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth()));
        return workHours;
    }

    public Integer getProfit(LocalDateTime startDate, LocalDateTime endDate, Long driverId) throws UserNotFoundException {
        List<RideDto> rides = rideService.getAllFinishedRides(driverId);

        Integer profit = 0;
        for (RideDto ride : rides) {
            LocalDateTime startRideTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            if ((startRideTime.isAfter(startDate) || startRideTime.equals(startDate)) && (startRideTime.isBefore(endDate) || startRideTime.equals(endDate))) {
                profit = profit + ride.getTotalCost();
            }
        }
        return profit;
    }

    public List<RideDto> getFinishedRides(LocalDateTime startDate, LocalDateTime endDate, Long userId) throws UserNotFoundException {
        List<RideDto> rides = rideService.getAllFinishedRides(userId);

        List<RideDto> acceptedRides = new ArrayList<>();
        for (RideDto ride : rides) {
            LocalDateTime startRideTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            if ((startRideTime.isAfter(startDate) || startRideTime.equals(startDate)) && (startRideTime.isBefore(endDate) || startRideTime.equals(endDate))) {
                acceptedRides.add(ride);
            }
        }
        return acceptedRides;
    }

    public List<RideDto> getRejectedRides(LocalDateTime startDate, LocalDateTime endDate, Long userId) throws UserNotFoundException {
        List<RideDto> rides = rideService.getAllRejectedRides(userId);
        List<RideDto> rejectedRides = new ArrayList<>();
        for (RideDto ride : rides) {
            LocalDateTime startRideTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            if ((startRideTime.isAfter(startDate) || startRideTime.equals(startDate)) && (startRideTime.isBefore(endDate) || startRideTime.equals(endDate))) {
                rejectedRides.add(ride);
            }
        }
        return rejectedRides;
    }


    public Map<LocalDate, Integer> getNumberOfRidesPerDay(LocalDateTime startDate, LocalDateTime endDate, Long userId) throws UserNotFoundException {

        Map<LocalDate, Integer> numberOfRidesPerDay = new HashMap<>();
        List<RideDto> finishedRides = rideService.getAllFinishedRides(userId);
        List<RideDto> rejectedRides = rideService.getAllRejectedRides(userId);
        List<RideDto> canceledRides = getAllCanceledRides(userId);

        List<RideDto> rides = new ArrayList<>();
        rides.addAll(finishedRides);
        rides.addAll(rejectedRides);
        rides.addAll(canceledRides);

        for (RideDto ride : rides) {
            LocalDateTime rideDateTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            LocalDate date = LocalDate.from(rideDateTime);

            if ((rideDateTime.isAfter(startDate) || rideDateTime.equals(startDate)) && (rideDateTime.isBefore(endDate) || rideDateTime.equals(endDate))) {
                if (numberOfRidesPerDay.containsKey(date)) {
                    numberOfRidesPerDay.replace(date, numberOfRidesPerDay.get(date) + 1);
                } else {
                    numberOfRidesPerDay.put(date, 1);
                }
            }
        }

        LocalDate localStartDate = LocalDate.from(startDate);
        LocalDate localEndDate = LocalDate.from(endDate);
        while (!localStartDate.equals(localEndDate)) {
            if (!numberOfRidesPerDay.containsKey(localStartDate)) {
                numberOfRidesPerDay.put(localStartDate, 0);
            }
            localStartDate = localStartDate.plusDays(1);
        }

        return numberOfRidesPerDay;
    }

    public Map<LocalDate, Integer> getProfitPerDay(LocalDateTime startDate, LocalDateTime endDate, Long driverId) throws UserNotFoundException {
        Map<LocalDate, Integer> profitPerDay = new HashMap<>();
        List<RideDto> finishedRides = rideService.getAllFinishedRides(driverId);
        for (RideDto ride : finishedRides) {
            LocalDateTime rideDateTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            LocalDate date = LocalDate.from(rideDateTime);

            if ((rideDateTime.isAfter(startDate) || rideDateTime.equals(startDate)) && (rideDateTime.isBefore(endDate) || rideDateTime.equals(endDate))) {
                if (profitPerDay.containsKey(date)) {
                    profitPerDay.replace(date, profitPerDay.get(date) + ride.getTotalCost());
                } else {
                    profitPerDay.put(date, ride.getTotalCost());
                }
            }
        }

        LocalDate localStartDate = LocalDate.from(startDate);
        LocalDate localEndDate = LocalDate.from(endDate);
        while (!localStartDate.equals(localEndDate)) {
            if (!profitPerDay.containsKey(localStartDate)) {
                profitPerDay.put(localStartDate, 0);
            }
            localStartDate = localStartDate.plusDays(1);
        }

        return profitPerDay;
    }

    public Map<LocalDate, Integer> getTraveledDistancePerDay(LocalDateTime startDate, LocalDateTime endDate, Long userId) throws UserNotFoundException {
        Map<LocalDate, Integer> traveledDistancePerDay = new HashMap<>();
        List<RideDto> rides = rideService.getAllFinishedRides(userId);
        for (RideDto ride : rides) {
            LocalDateTime rideDateTime = LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat);
            LocalDate date = LocalDate.from(rideDateTime);

            if ((rideDateTime.isAfter(startDate) || rideDateTime.equals(startDate)) && (rideDateTime.isBefore(endDate) || rideDateTime.equals(endDate))) {
                if (traveledDistancePerDay.containsKey(date)) {
                    traveledDistancePerDay.replace(date, traveledDistancePerDay.get(date) + calculateDistance(ride));
                } else {
                    traveledDistancePerDay.put(date, calculateDistance(ride));
                }
            }
        }

        LocalDate localStartDate = LocalDate.from(startDate);
        LocalDate localEndDate = LocalDate.from(endDate);
        while (!localStartDate.equals(localEndDate)) {
            if (!traveledDistancePerDay.containsKey(localStartDate)) {
                traveledDistancePerDay.put(localStartDate, 0);
            }
            localStartDate = localStartDate.plusDays(1);
        }


        return traveledDistancePerDay;
    }

    private Integer calculateDistance(RideDto ride) {
        Integer travelledDistance = 0;
        for (LocationForRideDto locationForRideDto : ride.getLocations()) {
            travelledDistance = travelledDistance + locationForRideDto.getDistanceInMeters();
        }
        return travelledDistance;
    }

    // Ovde sam je doda da ne bi je testirali
    private List<RideDto> getAllCanceledRides(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Collection<RideDto> rides = new ArrayList<>();

        if (user instanceof Driver driver) {
            rideRepository.findRidesByDriverId(driver.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        } else if (user instanceof Passenger passenger) {
            rideRepository.findRidesByPassengersId(passenger.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        } else {
            throw new UserNotFoundException("Only driver or passenger can have rides!");
        }

        List<RideDto> rejectedRides = new ArrayList<>();
        for (RideDto ride : rides) {
            if (ride.getStatus().equals("CANCELED")) {
                rejectedRides.add(ride);
            }
        }

        return rejectedRides;
    }
}
