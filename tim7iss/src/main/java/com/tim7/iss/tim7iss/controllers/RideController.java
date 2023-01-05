package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public ResponseEntity<RideDto> save(@Valid @RequestBody RideCreationDto rideRequestDto) {
        Ride ride = savePassengersAndDrivers(rideRequestDto);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<RideDto> scheduleRide(@Valid @RequestBody RideCreationDto rideCreationDto) throws Exception {
        // driver that doesn't have an active ride at the moment
        Driver availablePotentialDriver = null;

        // driver that has an active ride at the moment but is 5 minutes away from finishing
        Driver currentlyUnavailablePotentialDriver = null;

        // initializing the ride according to the ride creation dto
        Ride rideToSchedule = new Ride(rideCreationDto);

        // throwing error if the date is in the past
        if (rideToSchedule.getStartTime().isBefore(LocalDateTime.now())) {
            throw new Exception("Cannot schedule ride in the past");
        }

        // additional necessary information
        Location rideStartLocation = new Location(rideCreationDto.getLocations().get(0).getDeparture());
        LocalDateTime estimatedRequestedEndTime = rideToSchedule.getStartTime().plusMinutes(rideToSchedule.getEstimatedTimeInMinutes());

        for (Driver driver : driverService.getAll()) {
            // checking if the requested ride is requested to be scheduled during the work hours of the driver
            if (workHourService.getBetweenStartDateAndEndDateByDriverId(driver.getId(), rideToSchedule.getStartTime()).size() == 0) {
                continue;
            }

            // checking if the requested ride will end during the work hours of the driver
            if (workHourService.getBetweenStartDateAndEndDateByDriverId(driver.getId(), estimatedRequestedEndTime).size() == 0) {
                continue;
            }

            // checking to see if the ride that is about to get scheduled is going to overlap with an already existing ride
            Ride scheduledRideAtEstimatedEndTime = rideService.driverRideAtMoment(driver.getId(), rideToSchedule.getStartTime());

            // if estimated end time is overlapping with an existing ride; check next driver
            if (scheduledRideAtEstimatedEndTime != null) {
                continue;
            }

            // getting the ride scheduled at the time this passenger is requesting
            Ride alreadyScheduledRide = rideService.driverRideAtMoment(driver.getId(), rideToSchedule.getStartTime());

            // if there is a ride already scheduled at that date; check next driver
            if (alreadyScheduledRide != null) {
                /* driver is put into consideration to be given the new scheduled ride
                 * only if he is 5 minutes away from finishing his currently active ride */
                if (alreadyScheduledRide.getStatus() == Enums.RideStatus.ACTIVE) {
                    LocalDateTime estimatedRideEndTime = alreadyScheduledRide.getStartTime().plusMinutes(alreadyScheduledRide.getEstimatedTimeInMinutes());
                    long minutesUntil = alreadyScheduledRide.getStartTime().until(estimatedRideEndTime, ChronoUnit.MINUTES);
                    if (minutesUntil <= 5) {
                        if (currentlyUnavailablePotentialDriver == null) {
                            currentlyUnavailablePotentialDriver = driver;
                        } else {
                            // picking the unavailable driver who is closer to the start point of the ride
                            long currentPotentialDriverDistance = Constants.calculateDistance(rideStartLocation, currentlyUnavailablePotentialDriver.getVehicle().getLocation());
                            long nextPotentialDriverDistance = Constants.calculateDistance(rideStartLocation, driver.getVehicle().getLocation());
                            if (nextPotentialDriverDistance < currentPotentialDriverDistance) {
                                currentlyUnavailablePotentialDriver = driver;
                            }
                        }
                    }
                }
                continue;
            }

            if (availablePotentialDriver == null) {
                availablePotentialDriver = driver;
            } else {
                // picking the available driver who is closer to the start point of the ride
                long currentPotentialDriverDistance = Constants.calculateDistance(rideStartLocation, availablePotentialDriver.getVehicle().getLocation());
                long nextPotentialDriverDistance = Constants.calculateDistance(rideStartLocation, driver.getVehicle().getLocation());
                if (nextPotentialDriverDistance < currentPotentialDriverDistance) {
                    availablePotentialDriver = driver;
                }
            }

        }

        // priority is given to the driver that is currently available
        if (availablePotentialDriver != null) {
            rideToSchedule.setDriver(availablePotentialDriver);
        } else {
            if (currentlyUnavailablePotentialDriver != null) {
                rideToSchedule.setDriver(currentlyUnavailablePotentialDriver);

                // if the driver is initially busy then the start ride is moved by 5 minutes
                rideToSchedule.setStartTime(rideToSchedule.getStartTime().plusMinutes(5));
            } else {
                throw new DriverNotFoundException("There are no available drivers at that moment");  // TODO: Create a custom exception for busy drivers
            }
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
        Ride ride = rideService.findByPassengerIdAndStatus(passengerId, Enums.RideStatus.ACTIVE.ordinal());
        if (ride == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
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


    public Ride savePassengersAndDrivers(RideCreationDto rideRequestDto) {
        Ride ride = new Ride(rideRequestDto);

        // setting vehicle type for the ride
        ride.setVehicleType(vehicleTypeService.getByName(rideRequestDto.getVehicleType()));

        // setting the price
        int totalDistance = 0;
        for (Route r : ride.getRoutes()) {
            totalDistance += r.getDistanceInMeters();
            System.err.println(r);
        }
        ride.setPrice(ride.getVehicleType().getPricePerKm() + totalDistance * 120);

        // adding passengers to ride
        for (UserRefDto passengerRef : rideRequestDto.getPassengers()) {
            Passenger passenger = passengerService.findById(passengerRef.getId());
            if (passenger == null) {
                continue;
            }
            passenger.getRides().add(ride);
            ride.getPassengers().add(passenger);
        }

        // adding driver to ride
        Driver driver = driverService.findById(2L);
        if (driver != null) {
            ride.setDriver(driver);
        }

        rideService.save(ride);

        return ride;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/scheduled/ride")
    public Map<String, Object> sendScheduledRide(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            this.simpMessagingTemplate.convertAndSend("/socket-scheduled-ride", socketMessageConverted);
        }

        return socketMessageConverted;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/ride/evaluation")
    public Map<String, Object> sendRide(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            this.simpMessagingTemplate.convertAndSend("/socket-ride-evaluation", socketMessageConverted);
        }

        return socketMessageConverted;
    }

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }


}
