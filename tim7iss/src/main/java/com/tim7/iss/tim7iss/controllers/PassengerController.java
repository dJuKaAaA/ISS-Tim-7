package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.Exceptions.BadRequestException;
import com.tim7.iss.tim7iss.RequestDTOs.PassengerRequestDTO;
import com.tim7.iss.tim7iss.ResponseDTOs.PassengerResponseListDTO;
import com.tim7.iss.tim7iss.ResponseDTOs.PassengerResponseDTO;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("api/passenger")
public class PassengerController{
    @Autowired
    PassengerService passengerService;
    @PostMapping
    public ResponseEntity<PassengerResponseDTO> save(@RequestBody PassengerRequestDTO passengerRequestDTO) throws BadRequestException {
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

}
