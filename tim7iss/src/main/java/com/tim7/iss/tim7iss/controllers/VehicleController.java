package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {

        Vehicle vehicle = vehicleService.findById(id);

        // course must exist
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }
}
