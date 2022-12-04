package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.VehicleTypeRepository;
import com.tim7.iss.tim7iss.requestDTOs.PanicReasonDTO;
import com.tim7.iss.tim7iss.requestDTOs.RideRequestDTO;
import com.tim7.iss.tim7iss.responseDTOs.ErrorDTO;
import com.tim7.iss.tim7iss.responseDTOs.PanicDTO;
import com.tim7.iss.tim7iss.responseDTOs.RideResponseDTO;
import com.tim7.iss.tim7iss.responseDTOs.RideUserDTO;
import com.tim7.iss.tim7iss.services.*;
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

    @Autowired
    PanicService panicService;

    @Autowired
    VehicleTypeService vehicleTypeService;

    @PostMapping
    public ResponseEntity<RideResponseDTO>save(@RequestBody RideRequestDTO rideRequestDTO){
        RideResponseDTO response = new RideResponseDTO(rideRequestDTO);
        savePassengersAndDrivers(rideRequestDTO, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/active/driver/{driverId}")
    public ResponseEntity<RideResponseDTO> getDriversActiveRide(@PathVariable Long driverId){
        Ride ride = ridesService.findByDriverIdAndStatus(driverId, Enums.RideStatus.ACTIVE.ordinal());
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }
    @GetMapping(value = "/active/passenger/{passengerId}")
    public ResponseEntity<RideResponseDTO> getPassengersActiveRide(@PathVariable Long passengerId){
        Ride ride = ridesService.findByPassengerIdAndStatus(passengerId, Enums.RideStatus.ACTIVE.ordinal());
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<RideResponseDTO> getRideById(@PathVariable Long id){
        Ride ride = ridesService.findById(id);
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String>cancelRideById(@PathVariable Long id){
        Ride ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.PENDING.ordinal());
        if (ride == null){
            ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.ACCEPTED.ordinal());
            if(ride == null)
                return new ResponseEntity<>("Ride does not exist", HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.CANCELED);
        ridesService.save(ride);
        return new ResponseEntity<>("Ride successfully canceled", HttpStatus.OK);
    }

    @PutMapping(value = "/{rideId}/panic")
    public ResponseEntity<PanicDTO>creatingPanicProcedure(@RequestBody PanicReasonDTO reason, @PathVariable Long rideId){
        User user = passengerService.findById(1L);
        Ride ride = ridesService.findById(rideId);
        Panic panic = new Panic(reason,ride,user);
        panicService.save(panic);
        return new ResponseEntity<>(new PanicDTO(panic), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/accept")
    public ResponseEntity<RideResponseDTO>acceptRide(@PathVariable Long id){
        Ride ride = ridesService.findById(id);
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.ACCEPTED);
        ridesService.save(ride);
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/end")
    public ResponseEntity<RideResponseDTO>endRide(@PathVariable Long id){
        Ride ride = ridesService.findById(id);
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.FINISHED);
        ridesService.save(ride);
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/cancel")
    public ResponseEntity<RideResponseDTO>acceptRide(@PathVariable Long id, @RequestBody ErrorDTO reason){
        Ride ride = ridesService.findById(id);
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.CANCELED);
        ridesService.save(ride);
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    public void savePassengersAndDrivers(RideRequestDTO rideRequestDTO, RideResponseDTO response){
        Ride ride = new Ride(rideRequestDTO);
        ride.setVehicleType(vehicleTypeService.findById(1L));
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
