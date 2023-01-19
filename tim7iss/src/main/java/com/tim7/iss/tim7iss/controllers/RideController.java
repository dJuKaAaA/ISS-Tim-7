package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
import com.tim7.iss.tim7iss.exceptions.SchedulingRideAtInvalidDateException;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.PassengerRepository;
import com.tim7.iss.tim7iss.services.*;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    FavoriteLocationService favoriteLocationService;
    @Autowired
    LocationService locationService;
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
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;
    @Autowired
    private PassengerRepository passengerRepository;

    @PostMapping
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<RideDto> scheduleRide(@Valid @RequestBody RideCreationDto rideCreationDto, @RequestHeader("Authorization") String authHeader) throws SchedulingRideAtInvalidDateException, DriverNotFoundException, RideAlreadyPendingException, PassengerNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String passengerEmail = tokenUtils.getEmailFromToken(token);
        Passenger passengerThatSubmitted = passengerService.findByEmailAddress(passengerEmail).orElseThrow(PassengerNotFoundException::new);
        rideCreationDto.getPassengers().add(new UserRefDto(passengerThatSubmitted));
        if (AnyRidesArePending(rideCreationDto.getPassengers()))
            throw new RideAlreadyPendingException();
        // driver that doesn't have an active ride at the moment
        Driver availablePotentialDriver = null;
        Integer distanceFromStartLocationAvailableDriver = null;

        // driver that has an active ride at the moment but is 5 minutes away from finishing
        Driver currentlyUnavailablePotentialDriver = null;
        List<Driver> currentlyUnavailablePotentialDrivers = new ArrayList<>();
        Integer distanceFromStartLocationUnavailableDriver = null;

        // initializing the ride according to the ride creation dto
        Ride rideToSchedule = new Ride(rideCreationDto);

        // if the ride is null; that means an immediate order
        if (rideToSchedule.getStartTime() == null) {
            rideToSchedule.setStartTime(LocalDateTime.now().plusMinutes(Constants.vehicleWaitTimeInMinutes + 1));
        }

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
                distanceFromStartLocationAvailableDriver = mapService.getDistance(rideStartLocation.getLatitude(), rideStartLocation.getLongitude(), availablePotentialDriver.getVehicle().getLocation().getLatitude(), availablePotentialDriver.getVehicle().getLocation().getLongitude());
            } else {
                // picking the available driver who is closer to the start point of the ride
                Integer nextPotentialDriverDistance = mapService.getDistance(rideStartLocation.getLatitude(), rideStartLocation.getLongitude(), driver.getVehicle().getLocation().getLatitude(), driver.getVehicle().getLocation().getLongitude());
                if (nextPotentialDriverDistance < distanceFromStartLocationAvailableDriver) {
                    availablePotentialDriver = driver;
                }
            }

        }

        // priority is given to the driver that is currently available
        if (availablePotentialDriver != null) {
            rideToSchedule.setDriver(availablePotentialDriver);
        } else {
            Driver driver = currentlyUnavailablePotentialDrivers.stream().max(Comparator.comparing((Driver d) -> mapService.getDistance(rideStartLocation.getLatitude(), rideStartLocation.getLongitude(), d.getVehicle().getLocation().getLatitude(), d.getVehicle().getLocation().getLongitude()))).orElse(null);

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

    private boolean AnyRidesArePending(List<UserRefDto> passengers) {
        for(UserRefDto passenger : passengers) {
            List<Ride> rides = rideService.findByPassengerIdAndStatus(passenger.getId(),Enums.RideStatus.PENDING.ordinal());
            if(rides.size() != 0)
                return true;
        }
        return false;
    }

    @PostMapping(value = "/favorites")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<FavoriteLocationDto> createFavoriteLocation(@Valid @RequestBody FavoriteLocationDto favoriteLocationDto,
                                                                     @RequestHeader("Authorization") String authHeader)
            throws TooManyFavoriteRidesException, PassengerNotFoundException, UserNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String passengerEmail = tokenUtils.getEmailFromToken(token);
        Passenger passengerThatSubmitted = passengerService.findByEmailAddress(passengerEmail).orElseThrow(PassengerNotFoundException::new);
        if(checkIfMoreThan9FavoriteLocations(passengerThatSubmitted.getId())){
            throw new TooManyFavoriteRidesException();
        }
        Set<Passenger>passengers = new HashSet<>();
        for(UserRefDto pass: favoriteLocationDto.getPassengers()){
            Passenger passenger = passengerService.findById(pass.getId());
            if(passenger == null)
                throw new UserNotFoundException();
            passengers.add(passengerService.findById(pass.getId()));
        }
        passengers.add(passengerThatSubmitted);
        VehicleType vehicleType = vehicleTypeService.getByName(favoriteLocationDto.getVehicleType());
        FavoriteLocation favoriteLocation = new FavoriteLocation(favoriteLocationDto, passengers, vehicleType,passengerThatSubmitted);
        favoriteLocation = favoriteLocationService.save(favoriteLocation);
        favoriteLocationDto.setId(favoriteLocation.getId());
        return new ResponseEntity<>(new FavoriteLocationDto(favoriteLocation), HttpStatus.OK);
    }

    @GetMapping(value = "/favorites")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<List<FavoriteLocationDto>> getFavoriteLocations(@RequestHeader(value = "Authorization") String authHeader){
        List<FavoriteLocation>favoriteLocations = favoriteLocationService.getAll();
        List<FavoriteLocationDto>favoriteLocationsDto = new ArrayList<>();
        for(FavoriteLocation favoriteLocation : favoriteLocations){
            favoriteLocationsDto.add(new FavoriteLocationDto(favoriteLocation));
        }
        return new ResponseEntity<>(favoriteLocationsDto, HttpStatus.OK);
    }

    @GetMapping(value = "/favorites/{id}")
    public ResponseEntity<FavoriteLocationDto>getFavoriteLocationById(@PathVariable Long id) throws FavoriteLocationNotFoundException {
        FavoriteLocation favoriteLocation = favoriteLocationService.findById(id);
        if(favoriteLocation == null)
            throw new FavoriteLocationNotFoundException();
        return new ResponseEntity<>(new FavoriteLocationDto(favoriteLocation), HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{id}/favorites")
    public ResponseEntity<List<FavoriteLocationDto>>getFavoriteLocationsByPassengerId(@PathVariable Long id){
        List<FavoriteLocation>favoriteLocations = favoriteLocationService.findByPassengerId(id);
        List<FavoriteLocationDto>favoriteLocationsDto = new ArrayList<>();
        for(FavoriteLocation favoriteLocation : favoriteLocations){
            favoriteLocationsDto.add(new FavoriteLocationDto(favoriteLocation));
        }
        return new ResponseEntity<>(favoriteLocationsDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/favorites/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER','ADMIN')")
    public ResponseEntity<String> deleteFavoriteLocation(@RequestHeader(value = "Authorization")String authHeader, @PathVariable Long id) throws FavoriteLocationNotFoundException {
        if(favoriteLocationService.findById(id) == null)
            throw new FavoriteLocationNotFoundException();
        favoriteLocationService.delete(id);
        return new ResponseEntity("Successful deletion of favorite location!", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<RideDto> getDriversActiveRide(@RequestHeader(value = "Authorization") String authHeader, @PathVariable Long driverId) throws UserNotFoundException, RideNotFoundException {
        driverService.getById(driverId).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        List<RideDto> rides = rideService.findByDriverIdAndStatus(driverId, Enums.RideStatus.ACTIVE.ordinal()).stream().map(RideDto::new).toList();
        if (rides.size() == 0) {
            throw new RideNotFoundException("Active ride does not exist!");
        }
        return new ResponseEntity<>(rides.get(0), HttpStatus.OK);
    }

    //Delete fixed id
    @GetMapping(value = "/passenger/{passengerId}/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    public ResponseEntity<RideDto> getPassengersActiveRide(@PathVariable Long passengerId, @RequestHeader(value = "Authorization") String authHeader) throws RideNotFoundException {
        List<Ride> rides = rideService.findByPassengerIdAndStatus(passengerId, Enums.RideStatus.ACTIVE.ordinal());
        if (rides.size() == 0) {
            throw new RideNotFoundException("Active ride does not exist!");
        }
        return new ResponseEntity<>(new RideDto(rides.get(0)), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
    public ResponseEntity<RideDto> getRideById(@RequestHeader(value = "Authorization") String authHeader, @PathVariable Long id) throws RideNotFoundException {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            throw new RideNotFoundException("Active ride does not exist!");
        }
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    //Voznja moze da se prekine samo ukoliko je stanje voznje pending ili accepted,
    //Radi testiranja validacija stanja je zakomentarisana
    @PutMapping(value = "/{id}/withdraw")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<RideDto> cancelRideById(@PathVariable Long id, @RequestHeader(value = "Authorization")String authHeader) throws RideNotFoundException, RideCancelationException {
        Ride ride = rideService.findById(id);
//        Ride ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.PENDING.ordinal());
        if (ride == null) {
            throw new RideNotFoundException("Ride does not exist");
        }
        if(ride.getStatus() != Enums.RideStatus.PENDING && ride.getStatus() != Enums.RideStatus.ACTIVE)
            throw new RideCancelationException();
        ride.setStatus(Enums.RideStatus.REJECTED);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "/{rideId}/panic")
    @PreAuthorize("hasAnyRole('PASSENGER','DRIVER')")
    public ResponseEntity<PanicDetailsDto> creatingPanicProcedure(@RequestBody PanicCreateDto reason, @PathVariable Long rideId, @RequestHeader("Authorization") String authHeader) throws RideNotFoundException, UserNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        User user = userService.findByEmailAddress(userEmail).orElseThrow(UserNotFoundException::new);
        Ride ride = rideService.findById(rideId);
        if (ride == null) {
            throw new RideNotFoundException();
        }
        Panic panic = new Panic(reason, ride, user);
        panicService.save(panic);
        return new ResponseEntity<>(new PanicDetailsDto(panic), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) throws RideCancelationException, UserNotFoundException, RideNotFoundException {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            throw new RideNotFoundException();
        }
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        User driver = driverService.getByEmailAddress(userEmail).orElseThrow(UserNotFoundException::new);
        if(ride.getDriver().getId() != driver.getId())
            throw new RideNotFoundException();
        if(ride.getStatus() != Enums.RideStatus.PENDING)
            throw new RideCancelationException("Cannot accept a ride that is not in status PENDING!");
        ride.setStatus(Enums.RideStatus.ACCEPTED);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/end")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideDto> endRide(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) throws RideNotFoundException, RideCancelationException, UserNotFoundException {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            throw new RideNotFoundException();
        }
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        User driver = driverService.getByEmailAddress(userEmail).orElseThrow(UserNotFoundException::new);
        if(ride.getDriver().getId() != driver.getId())
            throw new RideNotFoundException();
        if(ride.getStatus() != Enums.RideStatus.ACTIVE)
            throw new RideCancelationException("Cannot end a ride that is not in status STARTED!");
        ride.setStatus(Enums.RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());  // ride finishes as soon as its status is set to finished
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/cancel")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideDto> rejectRide(@PathVariable Long id, @Valid @RequestBody PanicCreateDto rideReject, @RequestHeader("Authorization") String authHeader) throws RideNotFoundException, RideCancelationException, UserNotFoundException {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            throw new RideNotFoundException();
        }
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        User driver = driverService.getByEmailAddress(userEmail).orElseThrow(UserNotFoundException::new);
        if(ride.getDriver().getId() != driver.getId())
            throw new RideNotFoundException();
        if(ride.getStatus() != Enums.RideStatus.PENDING && ride.getStatus() != Enums.RideStatus.ACCEPTED)
            throw new RideCancelationException("Cannot cancel a ride that is not in status PENDING or ACCEPTED!");
        Refusal refusal = new Refusal(rideReject);
        refusal.setRide(ride);
        ride.setRefusal(refusal);
        ride.setStatus(Enums.RideStatus.CANCELED);  // Ovo je nekad bilo REJECTED ali sam stavio na CANCELED jer ima vise smisla
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{id}/start")
    public ResponseEntity<RideDto> startRide(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) throws RideNotFoundException, RideCancelationException, UserNotFoundException {
        Ride ride = rideService.findById(id);
        if (ride == null) {
            throw new RideNotFoundException();
        }
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        User driver = driverService.getByEmailAddress(userEmail).orElseThrow(UserNotFoundException::new);
        if(ride.getDriver().getId() != driver.getId())
            throw new RideNotFoundException();
        if(ride.getStatus() != Enums.RideStatus.ACCEPTED)
            throw new RideCancelationException("Cannot start a ride that is not in status ACCEPTED!");
        ride.setStatus(Enums.RideStatus.ACTIVE);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }


    @PutMapping("/setDriver")
    public ResponseEntity<RideDto> setDriver(@Valid @RequestBody RideAddDriverDto rideAddDriverDto){
        Ride ride = rideService.findById(rideAddDriverDto.getRideId());
        Driver driver = driverService.findById(rideAddDriverDto.getDriverId());
        ride.setDriver(driver);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    public boolean checkIfMoreThan9FavoriteLocations(Long id){
        List<FavoriteLocation> locations = favoriteLocationService.findByPassengerId(id);
        if(locations.size() > 9)
            return true;
        return false;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/scheduled/ride")
    public Map<String, Object> sendScheduledRide(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            @SuppressWarnings("unchecked") List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
            for (Map<String, Object> passenger : passengers) {
                this.simpMessagingTemplate.convertAndSend("/socket-scheduled-ride/to-passenger/" + passenger.get("id"), socketMessageConverted);
            }
            @SuppressWarnings("unchecked") Map<String, Object> driver = (Map<String, Object>) socketMessageConverted.get("driver");
            this.simpMessagingTemplate.convertAndSend("/socket-scheduled-ride/to-driver/" + driver.get("id"), socketMessageConverted);
        }

        return socketMessageConverted;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/ride/evaluation")
    public Map<String, Object> sendRideEvaluation(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            @SuppressWarnings("unchecked") List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
            for (Map<String, Object> passenger : passengers) {
                this.simpMessagingTemplate.convertAndSend("/socket-ride-evaluation/" + passenger.get("id"), socketMessageConverted);
            }
        }

        return socketMessageConverted;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/notify/start/ride")
    public Map<String, Object> notifyStartRide(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            @SuppressWarnings("unchecked") List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
            for (Map<String, Object> passenger : passengers) {
                this.simpMessagingTemplate.convertAndSend("/socket-notify-start-ride/" + passenger.get("id"), socketMessageConverted);
            }
        }

        return socketMessageConverted;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/notify/arrived/at/departure")
    public Map<String, Object> notifyArrivedAtDeparture(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            @SuppressWarnings("unchecked") List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
            for (Map<String, Object> passenger : passengers) {
                this.simpMessagingTemplate.convertAndSend("/socket-notify-arrived-at-departure/" + passenger.get("id"), socketMessageConverted);
            }
        }

        return socketMessageConverted;


    }

    @PostMapping("/api/send/notification")
    public String sendNotification(@RequestBody String message) {
        this.simpMessagingTemplate.convertAndSend("/socket-send-notification", message);
        return message;
    }


//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }

}
