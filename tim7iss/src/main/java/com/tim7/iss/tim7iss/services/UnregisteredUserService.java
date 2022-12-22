package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.DTOs.AssumedRideCostDTO;
import com.tim7.iss.tim7iss.DTOs.OrderRideDTO;
import com.tim7.iss.tim7iss.dto.RideCreationDto;
import com.tim7.iss.tim7iss.dto.RideEstimationsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UnregisteredUserService {

    public ResponseEntity<RideEstimationsDto> getAssumedRideCost(RideCreationDto rideCreationDto) {
        return new ResponseEntity<>(new RideEstimationsDto(400, 400f), HttpStatus.OK);
    }
}
