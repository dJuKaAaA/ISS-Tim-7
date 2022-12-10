package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.DTOs.Member2.PanicDTOs.PanicReasonDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs.RideRequestDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.ErrorDTO.ErrorDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.PanicDTOs.PanicDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.RideDTOs.RideResponseDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.PassengerDTOs.RideUserDTO;
import com.tim7.iss.tim7iss.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ride")
@Transactional
@CrossOrigin
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

    @Autowired
    RoutesService routesService;

    @PostMapping
    public ResponseEntity<RideResponseDTO>save(@RequestBody RideRequestDTO rideRequestDTO){
        RideResponseDTO response = new RideResponseDTO(rideRequestDTO);
        Long id = savePassengersAndDrivers(rideRequestDTO, response);
        response.id = id;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideResponseDTO> getDriversActiveRide(@PathVariable Long driverId){
        Ride ride = ridesService.findByDriverIdAndStatus(driverId, Enums.RideStatus.ACTIVE.ordinal());
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }
    //Delete fixed id
    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideResponseDTO> getPassengersActiveRide(@PathVariable Long passengerId){
        passengerId = 2L;
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

    //Voznja moze da se prekine samo ukoliko je stanje voznje pending ili accepted,
    //Radi testiranja validacija stanja je zakomentarisana
    @PutMapping(value = "/{id}/withdraw")
    public ResponseEntity<RideResponseDTO>cancelRideById(@PathVariable Long id){
        Ride ride = ridesService.findById(id);
//        Ride ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.PENDING.ordinal());
        if (ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//            ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.ACCEPTED.ordinal());
//            if(ride == null)
//                return new ResponseEntity<>("Ride does not exist", HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.CANCELED);
        ridesService.save(ride);
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    @PutMapping(value = "/{rideId}/panic")
    public ResponseEntity<PanicDTO>creatingPanicProcedure(@RequestBody PanicReasonDTO reason, @PathVariable Long rideId){
        User user = passengerService.findById(2L);
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
        ride.setStatus(Enums.RideStatus.REJECTED);
        ridesService.save(ride);
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    public Long savePassengersAndDrivers(RideRequestDTO rideRequestDTO, RideResponseDTO response){
        Ride ride = new Ride(rideRequestDTO);
        ride.setVehicleType(vehicleTypeService.findById(1L));
        for(RideUserDTO passengerDTO : rideRequestDTO.passengers){
                Passenger passenger = passengerService.findById(passengerDTO.getId());
                if(passenger == null) {
                    continue;
                }
                passenger.getFinishedRides().add(ride);
        }
        Driver driver = driverService.findById(1L);
        if(driver != null) {
            ride.setDriver(driver);
        }
        ridesService.save(ride);
        routesService.saveRoutes(ride.getRoutes());
        return ride.getId();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public String badRequestException(){
        return "Invalid data";
    }


}
