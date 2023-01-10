package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.User;
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
import javax.validation.Valid;
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
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto passengerRequestDto) {
        Passenger passenger = new Passenger(passengerRequestDto);
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
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto passengerRequestDto, @PathVariable Long id) throws UserNotFoundException {
        Passenger passenger = passengerService.findById(id);
        if (passenger == null) throw new UserNotFoundException() {
        };
        Passenger updatedPassenger = new Passenger(passengerRequestDto);
        updatedPassenger.setId(passenger.getId());
        passengerService.save(updatedPassenger);
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

    /* Treba mi za front za prikaz jednostavnih informacija o putniku u driver home page
     * kad driver klikne na email u kartici za ride
     * - Djukanovic
     */
    @GetMapping("/{id}/narrowed-profile-info")
    public ResponseEntity<PassengerNarrowedProfileInfoDto> getNarrowedInfo(@PathVariable Long id) throws UserNotFoundException {
        Passenger passenger = passengerService.findById(id);
        if (passenger == null) {
            throw new UserNotFoundException("Passenger not found");
        }
        return new ResponseEntity<>(new PassengerNarrowedProfileInfoDto(passenger), HttpStatus.OK);
    }

    @PostMapping("/by-email")
    public ResponseEntity<UserRefDto> fetchPassengerByEmail(@Valid @RequestBody UserRefDto passenger) throws UserNotFoundException {
        Passenger passengerByEmail = passengerService.findByEmailAddress(passenger.getEmail()).orElseThrow(() -> new UserNotFoundException("Passenger not found"));
        return new ResponseEntity<>(new UserRefDto(passengerByEmail), HttpStatus.OK);
    }

}
