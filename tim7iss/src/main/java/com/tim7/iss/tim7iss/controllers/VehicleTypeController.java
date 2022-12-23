package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.models.VehicleType;
import com.tim7.iss.tim7iss.services.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/vehicleType")
@CrossOrigin
@Transactional
public class VehicleTypeController {

    @Autowired
    VehicleTypeService vehicleTypeService;

    @GetMapping
    public ResponseEntity<Collection<VehicleType>> findAll(){
        Collection<VehicleType> vehicleTypes = vehicleTypeService.findAll();
        return new ResponseEntity<>(vehicleTypes, HttpStatus.OK);
    }
}
