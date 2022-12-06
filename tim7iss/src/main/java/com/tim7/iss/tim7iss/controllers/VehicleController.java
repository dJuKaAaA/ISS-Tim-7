package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationResponseDTO;
import com.tim7.iss.tim7iss.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vehicle")
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

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll() {
        List<Vehicle> vehicles = vehicleService.findAll();
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<String>changeLocation(@PathVariable Long id, @RequestBody LocationResponseDTO location){
        Vehicle vehicle = vehicleService.findById(id);
        if(vehicle == null){
            return new ResponseEntity<>("Vehicle does not exist", HttpStatus.NOT_FOUND);
        }
        Location newLocation = new Location(location);
        vehicle.setLocation(newLocation);
        vehicleService.save(vehicle);
        return new ResponseEntity<>("Cordinates successfully updated", HttpStatus.OK);
    }

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }
}
