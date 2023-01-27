package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.RideDto;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    public void save(Ride ride) {
        rideRepository.save(ride);
    }

    public long countAll() {
        return rideRepository.count();
    }



    public List<Ride> findRideByPassengerId(Long id) {
        return rideRepository.findAll(id);
    }



    public Long countAllByPassengerId(Long passengerId) {
        return rideRepository.countByPassengersId(passengerId);
    }

    //Pitati zasto ovo ne radi
    public List<Ride> findByDriverIdAndStatus(Long id, Integer status) {
        return rideRepository.findByDriverIdAndStatus(id, status);
    }

    public List<Ride> findByPassengerIdAndStatus(Long id, Integer status) {
        return rideRepository.findByPassengersIdAndStatus(id, status);
    }

//    public Ride findByPassengerIdAndStatus(Long id, Enums.RideStatus status){
//        return rideRepository.findByPassengerIdAndStatus(id, status);
//    }

    public Ride findById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public Ride findByStatus(Enums.RideStatus status) {
        return rideRepository.findByStatus(status);
    }

    public Ride driverRideAtMoment(Long driverId, LocalDateTime moment) {
        List<Ride> rides = rideRepository.findRidesByDriverId(driverId);
        rides = rides.stream().filter((Ride ride) -> {
            if (ride.getStatus() == Enums.RideStatus.ACCEPTED || ride.getStatus() == Enums.RideStatus.PENDING || ride.getStatus() == Enums.RideStatus.ACTIVE) {
                LocalDateTime rideStartTime = ride.getStartTime();
                LocalDateTime estimatedRideEndTime = ride.getStartTime().plusMinutes(ride.getEstimatedTimeInMinutes());

                // the time window for in which a ride is considered to be taken is between 5 minutes before start date and 5 minutes after end date
                return !moment.isBefore(rideStartTime.minusMinutes(Constants.vehicleWaitTimeInMinutes)) && !moment.isAfter(estimatedRideEndTime.plusMinutes(Constants.vehicleWaitTimeInMinutes));
            }
            return false;
        }).toList();
        return rides.size() == 0 ? null : rides.get(0);
    }

    public List<RideDto> getAllFinishedRides(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Collection<RideDto> rides = new ArrayList<>();

        if (user instanceof Driver driver) {
            rideRepository.findRidesByDriverId(driver.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        } else if (user instanceof Passenger passenger) {
            rideRepository.findRidesByPassengersId(passenger.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        }

        List<RideDto> finishedRides = new ArrayList<>();
        for (RideDto ride : rides) {
            if (ride.getStatus().equals("FINISHED")) {
                finishedRides.add(ride);
            }
        }

        return finishedRides;
    }


    public List<RideDto> getAllRejectedRides(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Collection<RideDto> rides = new ArrayList<>();

        if (user instanceof Driver driver) {
            rideRepository.findRidesByDriverId(driver.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        } else if (user instanceof Passenger passenger) {
            rideRepository.findRidesByPassengersId(passenger.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        }

        List<RideDto> finishedRides = new ArrayList<>();
        for (RideDto ride : rides) {
            if (ride.getStatus().equals("REJECTED")) {
                finishedRides.add(ride);
            }
        }

        return finishedRides;
    }


    public PaginatedResponseDto<RideDto> getPaginatedRidesForDriverAsDto(Long driverId, Pageable page)
            throws DriverNotFoundException {
        driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);
        List<RideDto> rides = rideRepository.findRidesByDriverId(driverId)
                .stream()
                .map(RideDto::new)
                .toList();
        return new PaginatedResponseDto<>(rides.size(), rides);
    }

    public Collection<RideDto> getScheduledRidesForDriverAsDto(Long driverId) throws DriverNotFoundException {
        driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);
        Collection<RideDto> pendingRides = rideRepository.
                findByDriverIdAndStatus(driverId, Enums.RideStatus.PENDING.ordinal())
                .stream()
                .map(RideDto::new)
                .toList();
        Collection<RideDto> acceptedRides = rideRepository
                .findByDriverIdAndStatus(driverId, Enums.RideStatus.ACCEPTED.ordinal())
                .stream()
                .map((Ride ride) -> {
                    ride.setRoutes(ride.getRoutes().stream().sorted(Comparator.comparing(Route::getId)).toList());
                    return new RideDto(ride);
                })
                .toList();
        Collection<RideDto> rides = new ArrayList<>();
        rides.addAll(pendingRides);
        rides.addAll(acceptedRides);
        return rides
                .stream()
                .sorted(Comparator.comparing(
                        (RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat)))
                .toList();
    }

}
