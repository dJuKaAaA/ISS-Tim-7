package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
import com.tim7.iss.tim7iss.exceptions.SchedulingRideAtInvalidDateException;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/ride")
@Transactional
@CrossOrigin
public class RideController {
    @Autowired
    PanicService panicService;
    @Autowired
    VehicleTypeService vehicleTypeService;
    @Autowired
    RoutesService routesService;
    @Autowired
    MapService mapService;
    @Autowired
    private RideService rideService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private WorkHourService workHourService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ResponseEntity<RideDto> scheduleRide(@Valid @RequestBody RideCreationDto rideCreationDto)
            throws SchedulingRideAtInvalidDateException, DriverNotFoundException {

        // driver that doesn't have an active ride at the moment
        Driver availablePotentialDriver = null;
        Integer distanceFromStartLocationAvailableDriver = null;

        // driver that has an active ride at the moment but is 5 minutes away from finishing
        Driver currentlyUnavailablePotentialDriver = null;
        List<Driver> currentlyUnavailablePotentialDrivers = new ArrayList<>();
        Integer distanceFromStartLocationUnavailableDriver = null;

        // initializing the ride according to the ride creation dto
        Ride rideToSchedule = new Ride(rideCreationDto);

        // throwing error if the schedule date is invalid
        if (rideToSchedule.getStartTime().isBefore(LocalDateTime.now().plusMinutes(Constants.vehicleWaitTimeInMinutes))) {
            throw new SchedulingRideAtInvalidDateException("Ride can only be scheduled " + Constants.vehicleWaitTimeInMinutes + " minutes from now or later");
        }

        // additional necessary information
        Location rideStartLocation = new Location(rideCreationDto.getLocations().get(0).getDeparture());
        LocalDateTime estimatedRequestedEndTime = rideToSchedule.getStartTime().plusMinutes(rideToSchedule.getEstimatedTimeInMinutes());

        for (Driver driver : driverService.getAll()) {

            // if driver is inactive: check next driver
            if (!driver.isActive()) {
                continue;
            }

            // if the has an ongoing shift but has worked more than 8 hours: check next driver
            if (workHourService.hoursWorked(driver.getId(), LocalDate.now()) >= 8) {
                continue;
            }

            // checking to see if the ride that is about to get scheduled is going to overlap with an already existing ride
            Ride scheduledRideAtEstimatedEndTime = rideService.driverRideAtMoment(driver.getId(), estimatedRequestedEndTime);

            // if estimated end time is overlapping with an existing ride: check next driver
            if (scheduledRideAtEstimatedEndTime != null) {
                continue;
            }

            // getting the ride scheduled at the time this passenger is requesting
            Ride alreadyScheduledRide = rideService.driverRideAtMoment(driver.getId(), rideToSchedule.getStartTime());

            // if there is a ride already scheduled at that date: check next driver
            if (alreadyScheduledRide != null) {
                /* driver is put into consideration to be given the new scheduled ride
                 * only if he is Constants.vehicleWaitTimeInMinutes minutes away from finishing his currently active ride */
                if (alreadyScheduledRide.getStatus() == Enums.RideStatus.ACTIVE) {
                    LocalDateTime estimatedRideEndTime = alreadyScheduledRide.getStartTime().plusMinutes(alreadyScheduledRide.getEstimatedTimeInMinutes());
                    long minutesUntil = LocalDateTime.now().until(estimatedRideEndTime, ChronoUnit.MINUTES);
                    if (minutesUntil >= 0 && minutesUntil <= Constants.vehicleWaitTimeInMinutes) {
                        currentlyUnavailablePotentialDrivers.add(driver);
                    }
                }
                continue;
            }

            if (availablePotentialDriver == null) {
                availablePotentialDriver = driver;
                distanceFromStartLocationAvailableDriver = mapService.getDistance(
                        rideStartLocation.getLatitude(),
                        rideStartLocation.getLongitude(),
                        availablePotentialDriver.getVehicle().getLocation().getLatitude(),
                        availablePotentialDriver.getVehicle().getLocation().getLongitude());
            } else {
                // picking the available driver who is closer to the start point of the ride
                Integer nextPotentialDriverDistance = mapService.getDistance(
                        rideStartLocation.getLatitude(),
                        rideStartLocation.getLongitude(),
                        driver.getVehicle().getLocation().getLatitude(),
                        driver.getVehicle().getLocation().getLongitude());
                if (nextPotentialDriverDistance < distanceFromStartLocationAvailableDriver) {
                    availablePotentialDriver = driver;
                }
            }

        }

        // priority is given to the driver that is currently available
        if (availablePotentialDriver != null) {
            rideToSchedule.setDriver(availablePotentialDriver);
        } else {
            Driver driver = currentlyUnavailablePotentialDrivers
                    .stream()
                    .max(Comparator.comparing((Driver d) -> mapService.getDistance(
                            rideStartLocation.getLatitude(),
                            rideStartLocation.getLongitude(),
                            d.getVehicle().getLocation().getLatitude(),
                            d.getVehicle().getLocation().getLongitude())))
                    .orElse(null);

            if (driver == null) {
                throw new DriverNotFoundException("There are no available drivers at that moment");
            }

            rideToSchedule.setDriver(driver);

            // if the driver is initially busy then the start ride is moved by 5 minutes
            rideToSchedule.setStartTime(rideToSchedule.getStartTime().plusMinutes(Constants.vehicleWaitTimeInMinutes));
        }

        // setting vehicle type for the ride
        rideToSchedule.setVehicleType(vehicleTypeService.getByName(rideCreationDto.getVehicleType()));

        // setting the price
        int totalDistance = 0;
        for (Route r : rideToSchedule.getRoutes()) {
            totalDistance += r.getDistanceInMeters();
        }
        rideToSchedule.setPrice(Math.round(rideToSchedule.getVehicleType().getPricePerKm() + totalDistance / 1000 * 120));

        // adding passengers to ride
        for (UserRefDto passengerRef : rideCreationDto.getPassengers()) {
            Passenger passenger = passengerService.findById(passengerRef.getId());
            if (passenger == null) {
                continue;
            }
            passenger.getRides().add(rideToSchedule);
            rideToSchedule.getPassengers().add(passenger);
        }

        // scheduling the ride by sending it to the database
        rideService.save(rideToSchedule);
        return new ResponseEntity<>(new RideDto(rideToSchedule), HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideDto> getDriversActiveRide(@PathVariable Long driverId) throws UserNotFoundException, RideNotFoundException {
        driverService.getById(driverId).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        List<RideDto> rides = rideService.findByDriverIdAndStatus(driverId, Enums.RideStatus.ACTIVE.ordinal())
                .stream()
                .map(RideDto::new)
                .toList();
        if (rides.size() == 0) {
            throw new RideNotFoundException("Driver has no active rides at the moment");
        }
        return new ResponseEntity<>(rides.get(0), HttpStatus.OK);
    }

    //Delete fixed id
    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideDto> getPassengersActiveRide(@PathVariable Long passengerId) {
        List<Ride> rides = rideService.findByPassengerIdAndStatus(passengerId, Enums.RideStatus.ACTIVE.ordinal());
        if (rides.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideDto(rides.get(0)), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<RideDto> getRideById(@PathVariable Long id) {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    //Voznja moze da se prekine samo ukoliko je stanje voznje pending ili accepted,
    //Radi testiranja validacija stanja je zakomentarisana
    @PutMapping(value = "/{id}/withdraw")
    public ResponseEntity<RideDto> cancelRideById(@PathVariable Long id) throws RideNotFoundException {
        Ride ride = rideService.findById(id);
//        Ride ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.PENDING.ordinal());
        if (ride == null) {
            throw new RideNotFoundException();
//            ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.ACCEPTED.ordinal());
//            if(ride == null)
//                return new ResponseEntity<>("Ride does not exist", HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.REJECTED);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "/{rideId}/panic")
    public ResponseEntity<PanicDetailsDto> creatingPanicProcedure(@RequestBody PanicCreateDto reason, @PathVariable Long rideId) {
        User user = passengerService.findById(3L);
        Ride ride = rideService.findById(rideId);
        Panic panic = new Panic(reason, ride, user);
        panicService.save(panic);
        return new ResponseEntity<>(new PanicDetailsDto(panic), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/accept")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long id) {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.ACCEPTED);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/end")
    public ResponseEntity<RideDto> endRide(@PathVariable Long id) throws RideNotFoundException {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            throw new RideNotFoundException();
        }
        ride.setStatus(Enums.RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());  // ride finishes as soon as its status is set to finished
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/cancel")
    public ResponseEntity<RideDto> rejectRide(@PathVariable Long id, @Valid @RequestBody RideRejectDto rideReject) throws RideNotFoundException {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            throw new RideNotFoundException();
        }
        Refusal refusal = new Refusal(rideReject);
        refusal.setRide(ride);
        ride.setRefusal(refusal);
        ride.setStatus(Enums.RideStatus.CANCELED);  // Ovo je nekad bilo REJECTED ali sam stavio na CANCELED jer ima vise smisla
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<RideDto> startRide(@PathVariable Long id) throws RideNotFoundException {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            throw new RideNotFoundException();
        }
        ride.setStatus(Enums.RideStatus.ACTIVE);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }


    @PutMapping("/setDriver")
    public ResponseEntity<RideDto> setDriver(@RequestBody RideAddDriverDto rideAddDriverDto) {
        Ride ride = rideService.findById(rideAddDriverDto.getRideId());
        Driver driver = driverService.findById(rideAddDriverDto.getDriverId());
        ride.setDriver(driver);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/scheduled/ride")
    public Map<String, Object> sendScheduledRide(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
            for (Map<String, Object> passenger : passengers) {
                this.simpMessagingTemplate.convertAndSend("/socket-scheduled-ride/to-passenger/" + passenger.get("id"),
                        socketMessageConverted);
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> driver = (Map<String, Object>) socketMessageConverted.get("driver");
            this.simpMessagingTemplate.convertAndSend("/socket-scheduled-ride/to-driver/" + driver.get("id"),
                    socketMessageConverted);
        }

        return socketMessageConverted;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/ride/evaluation")
    public Map<String, Object> sendRideEvaluation(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
            for (Map<String, Object> passenger : passengers) {
                this.simpMessagingTemplate.convertAndSend("/socket-ride-evaluation/" + passenger.get("id"),
                        socketMessageConverted);
            }
        }

        return socketMessageConverted;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/notify/start/ride")
    public Map<String, Object> notifyStartRide(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
            for (Map<String, Object> passenger : passengers) {
                this.simpMessagingTemplate.convertAndSend("/socket-notify-start-ride/" + passenger.get("id"),
                        socketMessageConverted);
            }
        }

        return socketMessageConverted;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/notify/arrived/at/departure")
    public Map<String, Object> notifyArrivedAtDeparture(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
            for (Map<String, Object> passenger : passengers) {
                this.simpMessagingTemplate.convertAndSend("/socket-notify-arrived-at-departure/" + passenger.get("id"),
                        socketMessageConverted);
            }
        }

        return socketMessageConverted;
    }


//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }


}
