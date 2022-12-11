package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.DTOs.AssumedRideCostDTO;
import com.tim7.iss.tim7iss.DTOs.OrderRideDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UnregisteredUserService {

    public ResponseEntity<AssumedRideCostDTO> getAssumedRideCost(OrderRideDTO orderRideDTO) {
        return new ResponseEntity<>(new AssumedRideCostDTO(400, 400), HttpStatus.OK);
    }
}
