package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.AssumedRideCostDTO;
import com.tim7.iss.tim7iss.DTOs.OrderRideDTO;
import com.tim7.iss.tim7iss.services.UnregisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class UnregisteredUserController {

    @Autowired
    UnregisteredUserService unregisteredUserService;

    @PostMapping("/api/unregisteredUser/")
    public ResponseEntity<AssumedRideCostDTO> getAssumedTimeAndCostOfRide(@RequestBody OrderRideDTO orderRideDTO) {
        return unregisteredUserService.getAssumedRideCost(orderRideDTO);
    }

}
