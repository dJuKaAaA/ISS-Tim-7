package com.tim7.iss.tim7iss.controllers;

import RequestDTOs.PassengerRequestDTO;
import ResponseDTOs.PassengerResponseDTO;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/passenger")
public class PassengerController {
    @Autowired
    PassengerService passengerService;
    @PostMapping
    public ResponseEntity<PassengerResponseDTO> save(@RequestBody PassengerRequestDTO passengerRequestDTO){
        Passenger passenger = new Passenger(passengerRequestDTO);
        passengerService.save(passenger);
        PassengerResponseDTO passengerDTO = new PassengerResponseDTO(passenger);
        return new ResponseEntity<>(passengerDTO, HttpStatus.OK);
    }
}
