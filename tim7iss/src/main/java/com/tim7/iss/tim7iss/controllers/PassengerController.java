package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.PassengerDTO;
import com.tim7.iss.tim7iss.models.Passenger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/passenger")
public class PassengerController {
    @PostMapping
    public ResponseEntity<PassengerDTO> save(@RequestBody Passenger passenger){
        PassengerDTO passengerDTO = new PassengerDTO(passenger);
        return new ResponseEntity<>(passengerDTO, HttpStatus.OK);
    }
}
