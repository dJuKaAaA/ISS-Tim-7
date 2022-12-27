package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.RideDto;
import com.tim7.iss.tim7iss.dto.UserDto;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.UserActivation;
import com.tim7.iss.tim7iss.services.PassengerService;
import com.tim7.iss.tim7iss.services.RideService;
import com.tim7.iss.tim7iss.services.UserActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("api/passenger")
@Transactional
@CrossOrigin
public class PassengerController {
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private UserActivationService userActivationService;
    @Autowired
    private RideService rideService;

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto passengerRequestDto) {
        Passenger passenger = new Passenger();
        passenger.setParameters(passengerRequestDto);
        int strength = 10; // work factor of bcrypt
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(passengerRequestDto.getPassword());
        passenger.setPassword(encodedPassword);
        passengerService.save(passenger);
        return new ResponseEntity<>(new UserDto(passenger), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<UserDto>> load(Pageable page) {
        Collection<UserDto> passengers = new ArrayList<>();
        for (Passenger passenger : passengerService.findAll(page)) {
            passengers.add(new UserDto(passenger));
        }
        return new ResponseEntity<>(new PaginatedResponseDto<>(passengers.size(), passengers), HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{activationId}")
    public ResponseEntity<String> activateUser(@PathVariable Long activationId) {
        UserActivation activation = userActivationService.findById(activationId);
        if (activation == null) {
            return new ResponseEntity<>("Activation with that id doesn't exist", HttpStatus.NOT_FOUND);
        }
        if (activation.getExpirationDate().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("This activation has expired", HttpStatus.BAD_REQUEST);
        }
        activation.getUser().setActive(true);
        userActivationService.deleteById(activationId);
        return new ResponseEntity<>("Successful account activation", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findPassengerByID(@PathVariable Long id) throws UserNotFoundException {
        Passenger passenger = passengerService.findById(id);
        if (passenger == null) throw new UserNotFoundException() {
        };
        return new ResponseEntity<>(new UserDto(passenger), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> save(@RequestBody UserDto passengerRequestDto, @PathVariable Long id) throws UserNotFoundException {
        Passenger passenger = passengerService.findById(id);
        if (passenger == null) throw new UserNotFoundException() {
        };
        passenger.setParameters(passengerRequestDto);
        passengerService.save(passenger);
        return new ResponseEntity<>(new UserDto(passenger), HttpStatus.OK);
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<PaginatedResponseDto<RideDto>> findRidesByPassengerId(@PathVariable Long id, Pageable page) {
//        ridesService.findByFilter();
        Collection<RideDto> rides = new ArrayList<>();
        for (Ride ride : rideService.findRideByPassengerId(id, page)) {
            rides.add(new RideDto(ride));
        }
        return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);
    }

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }
//
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    @ExceptionHandler(UserNotFoundException.class)
//    public String userNotFoundException(){
//        return "User not found";
//    }
}
