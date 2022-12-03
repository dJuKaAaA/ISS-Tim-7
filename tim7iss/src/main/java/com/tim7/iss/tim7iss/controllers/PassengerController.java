package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.requestDTOs.PassengerRequestDTO;
import com.tim7.iss.tim7iss.responseDTOs.PassengerResponseListDTO;
import com.tim7.iss.tim7iss.responseDTOs.PassengerResponseDTO;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.UserActivation;
import com.tim7.iss.tim7iss.responseDTOs.RidesFilterResponseDTO;
import com.tim7.iss.tim7iss.responseDTOs.RidesFilterResponseListDTO;
import com.tim7.iss.tim7iss.services.PassengerService;
import com.tim7.iss.tim7iss.services.RidesService;
import com.tim7.iss.tim7iss.services.UserActivationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/passenger")
@Transactional
public class PassengerController{
    @Autowired
    PassengerService passengerService;
    @Autowired
    UserActivationService userActivationService;
    @Autowired
    RidesService ridesService;

    @PostMapping
    public ResponseEntity<PassengerResponseDTO> save(@RequestBody PassengerRequestDTO passengerRequestDTO){
        Passenger passenger = new Passenger(passengerRequestDTO);
        passengerService.save(passenger);
        PassengerResponseDTO passengerDTO = new PassengerResponseDTO(passenger);
        return new ResponseEntity<>(passengerDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PassengerResponseListDTO> load(){
        List<Passenger> passengers = passengerService.findAll();
        PassengerResponseListDTO response = new PassengerResponseListDTO();
        for(Passenger passenger : passengers){
            response.results.add(new PassengerResponseDTO(passenger));
            response.totalCount += 1;
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/{activationId}")
    public ResponseEntity<String> activateUser(@PathVariable Long activationId){
        UserActivation activation = userActivationService.findById(activationId);
        if(activation == null){
            return new ResponseEntity<>("Activation with that id doesn't exist", HttpStatus.NOT_FOUND);
        }
        if(activation.getExpirationDate().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("This activation has expired", HttpStatus.BAD_REQUEST);
        }
        activation.getUser().setActive(true);
        userActivationService.deleteById(activationId);
        return new ResponseEntity<>("Successful account activation",HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PassengerResponseDTO> findPassengerByID(@PathVariable Long id) throws UserNotFoundException {
        Passenger passenger = passengerService.findById(id);
        if(passenger == null) throw new UserNotFoundException(){};
        return new ResponseEntity<>(new PassengerResponseDTO(passenger),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponseDTO> save(@RequestBody PassengerRequestDTO passengerRequestDTO, @PathVariable Long id) throws UserNotFoundException {
        Passenger passenger = passengerService.findById(id);
        if(passenger == null) throw new UserNotFoundException(){};
        passenger.setParameters(passengerRequestDTO);
        passengerService.save(passenger);
        return new ResponseEntity<>(new PassengerResponseDTO(passenger),HttpStatus.OK);
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<RidesFilterResponseListDTO> findRidesByPassengerId(@PathVariable Long id){
//        ridesService.findByFilter();
        List<Ride> rides = ridesService.findRideByPassengerId(id);
        RidesFilterResponseListDTO response = new RidesFilterResponseListDTO();
        for(Ride ride : rides){
            response.results.add(new RidesFilterResponseDTO(ride));
            response.totalCount += 1;
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
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
