package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.services.*;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<RideDto> scheduleRide(@Valid @RequestBody RideCreationDto rideCreationDto) throws SchedulingRideAtInvalidDateException, DriverNotFoundException, RideAlreadyPendingException, RideNotFoundException {
        RideDto ride = rideService.scheduleRide(rideCreationDto);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }


    @PostMapping(value = "/favorites")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<FavoriteLocationDto> createFavoriteLocation(@Valid @RequestBody FavoriteLocationDto favoriteLocationDto, @RequestHeader("Authorization") String authHeader) throws TooManyFavoriteRidesException, PassengerNotFoundException, UserNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String passengerEmail = tokenUtils.getEmailFromToken(token);
        FavoriteLocationDto newFavoriteLocationDto = rideService.createFavoriteLocation(passengerEmail, favoriteLocationDto);
        return new ResponseEntity<>(newFavoriteLocationDto, HttpStatus.OK);
    }

    // TESTIRANO
    @GetMapping(value = "/favorites")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<List<FavoriteLocationDto>> getFavoriteLocations() {
        List<FavoriteLocationDto> favoriteLocationsDto = rideService.getFavoriteLocations();
        return new ResponseEntity<>(favoriteLocationsDto, HttpStatus.OK);
    }

    @GetMapping(value = "/favorites/{id}")
    public ResponseEntity<FavoriteLocationDto> getFavoriteLocationById(@PathVariable Long id) throws FavoriteLocationNotFoundException {
        FavoriteLocationDto favoriteLocationDto = rideService.getFavoriteLocationById(id);
        return new ResponseEntity<>(favoriteLocationDto, HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{id}/favorites")
    public ResponseEntity<List<FavoriteLocationDto>> getFavoriteLocationsByPassengerId(@PathVariable Long id) throws UserNotFoundException {
        List<FavoriteLocationDto> favoriteLocationsDto = rideService.getFavoriteLocationsByPassengerId(id);
        return new ResponseEntity<>(favoriteLocationsDto, HttpStatus.OK);
    }

    // TESTIRANO
    @DeleteMapping(value = "/favorites/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER','ADMIN')")
    public ResponseEntity<String> deleteFavoriteLocation(@PathVariable Long id) throws FavoriteLocationNotFoundException {
        rideService.deleteFavoriteLocation(id);
        return new ResponseEntity("Successful deletion of favorite location!", HttpStatus.NO_CONTENT);
    }

    //Milos
    @GetMapping(value = "/driver/{driverId}/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<RideDto> getDriversActiveRide(@PathVariable Long driverId) throws UserNotFoundException, RideNotFoundException {
        List<RideDto> rides = rideService.getDriversActiveRide(driverId);
        return new ResponseEntity<>(rides.get(0), HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    public ResponseEntity<RideDto> getPassengersActiveRide(@PathVariable Long passengerId) throws RideNotFoundException {
        List<RideDto> rides = rideService.getPassengersActiveRide(passengerId);
        return new ResponseEntity<>(rides.get(0), HttpStatus.OK);
    }


    // TESTIRANO
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER', 'DRIVER')")
    public ResponseEntity<RideDto> getRideById(@PathVariable Long id) throws RideNotFoundException {
        RideDto ride = rideService.getRideById(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    //Milos
    @PutMapping(value = "/{id}/withdraw")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<RideDto> cancelRideById(@PathVariable Long id) throws RideNotFoundException, RideCancelationException {
        RideDto ride = rideService.cancelRideById(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    //Milos
    @PutMapping(value = "/{rideId}/panic")
    @PreAuthorize("hasAnyRole('PASSENGER','DRIVER')")
    public ResponseEntity<PanicDetailsDto> creatingPanicProcedure(@RequestBody PanicCreateDto reason, @PathVariable Long rideId, @RequestHeader("Authorization") String authHeader) throws RideNotFoundException, UserNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        PanicDetailsDto panic = rideService.creatingPanicProcedure(userService, panicService, userEmail, rideId, reason);
        return new ResponseEntity<>(panic, HttpStatus.OK);
    }


    // TESTIRANO
    @PutMapping(value = "{id}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) throws RideCancelationException, RideNotFoundException, DriverNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        RideDto ride = rideService.acceptRide(id, userEmail);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    // TESTIRANO

    @PutMapping(value = "{id}/end")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideDto> endRide(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) throws RideNotFoundException, RideCancelationException, DriverNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        RideDto ride = rideService.endRide(id, userEmail);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    //Milos
    @PutMapping(value = "{id}/cancel")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideDto> rejectRide(@PathVariable Long id, @Valid @RequestBody PanicCreateDto rideReject, @RequestHeader("Authorization") String authHeader) throws RideNotFoundException, RideCancelationException, DriverNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        RideDto ride = rideService.rejectRide(id, userEmail, rideReject);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    //Milos
    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/{id}/start")
    public ResponseEntity<RideDto> startRide(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) throws RideNotFoundException, RideCancelationException, DriverNotFoundException {
        String token = tokenUtils.getToken(authHeader);
        String userEmail = tokenUtils.getEmailFromToken(token);
        RideDto ride = rideService.startRide(id, userEmail);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }


    @PutMapping("/setDriver")
    public ResponseEntity<RideDto> setDriver(@Valid @RequestBody RideAddDriverDto rideAddDriverDto) throws DriverNotFoundException, RideNotFoundException {
        RideDto ride = rideService.setDriver(rideAddDriverDto);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }


    // NE TESTIRATI
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


    @Autowired
    private RideRepository rideRepository;
    @GetMapping("/everybody")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<RideDto>> getAllRides(Pageable pageable) {
        List<RideDto> rides = rideRepository.findAll(pageable)
                .stream()
                .map(RideDto::new)
                .toList();
        return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);
    }

}
