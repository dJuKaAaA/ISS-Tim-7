package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.RideCreationDto;
import com.tim7.iss.tim7iss.dto.RideEstimationsDto;
import com.tim7.iss.tim7iss.dto.UnregisteredUserRideCreation;
import com.tim7.iss.tim7iss.services.UnregisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Transactional
@Validated
public class UnregisteredUserController {

    @Autowired
    UnregisteredUserService unregisteredUserService;

    @PostMapping("/api/unregisteredUser")
    public ResponseEntity<RideEstimationsDto> getAssumedTimeAndCostOfRide(@Valid @RequestBody UnregisteredUserRideCreation unregisteredUserRideCreation) {
        return unregisteredUserService.getAssumedRideCost(unregisteredUserRideCreation);
    }

}
