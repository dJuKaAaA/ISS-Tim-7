package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.RideEstimationsDto;
import com.tim7.iss.tim7iss.dto.UnregisteredUserRideCreation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UnregisteredUserService {

    public ResponseEntity<RideEstimationsDto> getAssumedRideCost(@Valid UnregisteredUserRideCreation rideCreationDto) {
        return new ResponseEntity<>(new RideEstimationsDto(400, 400f), HttpStatus.OK);
    }
}
