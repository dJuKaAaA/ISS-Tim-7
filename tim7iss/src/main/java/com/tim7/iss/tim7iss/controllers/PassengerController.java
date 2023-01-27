package com.tim7.iss.tim7iss.controllers;

import ch.qos.logback.core.joran.spi.ActionException;
import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.models.UserActivation;
import com.tim7.iss.tim7iss.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private MailService mailService;
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto passengerRequestDto) throws EmailAlreadyExistsException {
        Optional p = passengerService.findByEmailAddress(passengerRequestDto.getEmail());
        if(!p.isEmpty()){
            throw new EmailAlreadyExistsException();
        }
        Passenger passenger = new Passenger(passengerRequestDto);
        String encodedPassword = bCryptPasswordEncoder.encode(passengerRequestDto.getPassword());
        passenger.setPassword(encodedPassword);
        passenger.setRoles(List.of(roleService.getRoleByName("ROLE_PASSENGER")));
        passengerService.save(passenger);
        UserActivation userActivation = userActivationService.save(new UserActivation(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), passenger));
        String activationMailBody = "Click on the link to activate your account: " + "http://localhost:8081/api/passenger/activate/" + userActivation.getId();
        mailService.sendTextEmail(passenger.getEmailAddress(), "Mail activation for GoGoCarJet", activationMailBody);
        return new ResponseEntity<>(new UserDto(passenger), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<PaginatedResponseDto<UserDto>> load(@RequestHeader(value = "Authorization") String authHeader, Pageable page) throws UnauthorizedException {
//        if(authHeader == null)
//            throw new UnauthorizedException();
        Collection<UserDto> passengers = new ArrayList<>();
        for (Passenger passenger : passengerService.findAll(page)) {
            passengers.add(new UserDto(passenger));
        }
        return new ResponseEntity<>(new PaginatedResponseDto<>(passengers.size(), passengers), HttpStatus.OK);
    }

    @GetMapping(value = "/activate/{activationId}")
    public ResponseEntity<ErrorDto> activateUser(@PathVariable Long activationId) throws ActivationNotFoundException, ActivationExpiredException {
        UserActivation activation = userActivationService.findById(activationId);
        if (activation == null) {
            throw new ActivationNotFoundException();
        }
        if (activation.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ActivationExpiredException();
        }
        activation.getPassenger().setEnabled(true);
        passengerService.save(activation.getPassenger());
        userActivationService.deleteById(activationId);
        return new ResponseEntity<>(new ErrorDto("Successful account activation!"), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER')")
    public ResponseEntity<UserDto> findPassengerByID(@RequestHeader(value = "Authorization") String authHeader, @PathVariable Long id) throws UserNotFoundException {
        Passenger passenger = passengerService.findById(id);
        if (passenger == null) throw new UserNotFoundException("Passenger does not exist!") {
        };
        return new ResponseEntity<>(new UserDto(passenger), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<UserDto> save(@RequestHeader(value = "Authorization") String authHeader, @Valid @RequestBody UserDto passengerRequestDto, @PathVariable Long id) throws UserNotFoundException {
        Passenger passenger = passengerService.findById(id);
        if (passenger == null) throw new UserNotFoundException("Passenger does not exist!") {
        };
        passenger.setFirstName(passengerRequestDto.getName());
        passenger.setLastName(passengerRequestDto.getSurname());
        passenger.setProfilePicture(passengerRequestDto.getProfilePicture());
        passenger.setPhoneNumber(passengerRequestDto.getTelephoneNumber());
        passenger.setEmailAddress(passengerRequestDto.getEmail());
        passenger.setAddress(passengerRequestDto.getAddress());
        passengerService.save(passenger);
        return new ResponseEntity<>(new UserDto(passenger), HttpStatus.OK);
    }

    @GetMapping("/{id}/ride")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<PaginatedResponseDto<RideDto>> findRidesByPassengerId(@RequestHeader(value = "Authorization") String authHeader, @PathVariable Long id, Pageable page) throws UserNotFoundException {
//        ridesService.findByFilter();
        if(passengerService.findById(id) == null)
            throw new UserNotFoundException("Passenger does not exist!");
        Collection<RideDto> rides = new ArrayList<>();
        for (Ride ride : rideService.findRideByPassengerId(id)) {
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

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getPassengerByEmail(@PathVariable("email") String email) throws PassengerNotFoundException {
        return passengerService.getPassengerByEmail(email);
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
