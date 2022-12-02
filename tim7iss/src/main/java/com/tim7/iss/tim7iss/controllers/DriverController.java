package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.DriverDTO;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("")
    public ResponseEntity<Collection<DriverDTO>> getAll() {
        Collection<Driver> allDrivers = driverService.getAll();
        Collection<DriverDTO> driverDTOs = new ArrayList<>();
        allDrivers.forEach(driver -> driverDTOs.add(new DriverDTO(driver)));
        return new ResponseEntity<>(
                driverDTOs,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getById(@PathVariable Long id) throws RuntimeException {
        Driver driver = driverService.getById(id);
        return new ResponseEntity<>(
                new DriverDTO(driver),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    public ResponseEntity<DriverDTO> save(@RequestBody Driver driver) {
        driverService.save(driver);
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
    }

}
