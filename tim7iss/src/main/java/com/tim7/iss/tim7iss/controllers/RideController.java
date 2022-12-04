package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.requestDTOs.RideRequestDTO;
import com.tim7.iss.tim7iss.responseDTOs.RideResponseDTO;
import com.tim7.iss.tim7iss.responseDTOs.RideUserDTO;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.PassengerService;
import com.tim7.iss.tim7iss.services.RidesService;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ride")
@Transactional
public class RideController {
    @Autowired
    RidesService ridesService;

    @Autowired
    PassengerService passengerService;

    @Autowired
    DriverService driverService;

    @PostMapping
    public ResponseEntity<RideResponseDTO>save(@RequestBody RideRequestDTO rideRequestDTO){
        RideResponseDTO response = new RideResponseDTO(rideRequestDTO);
        savePassengersAndDrivers(rideRequestDTO, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/active/{driverId}")
    public ResponseEntity<RideResponseDTO> getDriversActiveRide(@PathVariable Long driverId){
        Ride ride = ridesService.findRideByDriverIdAndStatusEquals3(driverId);
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    public void savePassengersAndDrivers(RideRequestDTO rideRequestDTO, RideResponseDTO response){
        Ride ride = new Ride(rideRequestDTO);
        Driver driver = null;
        for(RideUserDTO rideUserDTO : rideRequestDTO.passengers){
            if(rideUserDTO.getType().equals("PASSENGER")){
                Passenger passenger = passengerService.findById(rideUserDTO.getId());
                if(passenger == null) {
                    continue;
                }
                passenger.getFinishedRides().add(ride);
                response.passengers.add(new RideUserDTO(passenger, "PASSENGER"));
            }
            else{
                driver = driverService.findById(rideUserDTO.getId());
            }
        }
        if(driver != null) {
            ride.setDriver(driver);
            response.passengers.add(new RideUserDTO(driver, "DRIVER"));
        }
        ridesService.save(ride);
    }

}
