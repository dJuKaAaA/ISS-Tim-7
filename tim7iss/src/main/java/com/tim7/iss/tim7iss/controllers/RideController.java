package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/ride")
@Transactional
@CrossOrigin
public class RideController {
    @Autowired
    private RideService rideService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private DriverService driverService;

    @Autowired
    PanicService panicService;

    @Autowired
    VehicleTypeService vehicleTypeService;

    @Autowired
    RoutesService routesService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ResponseEntity<RideDto> save(@Valid @RequestBody RideCreationDto rideRequestDto){
        Ride ride = savePassengersAndDrivers(rideRequestDto);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideDto> getDriversActiveRide(@PathVariable Long driverId) throws UserNotFoundException, RideNotFoundException{
        driverService.getById(driverId).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        List<RideDto> rides = rideService.findByDriverIdAndStatus(driverId, Enums.RideStatus.ACTIVE.ordinal())
                .stream()
                .map(RideDto::new)
                .toList();
        if (rides.size() == 0) {
            throw new RideNotFoundException("Driver has no active rides at the moment");
        }
        return new ResponseEntity<>(rides.get(0), HttpStatus.OK);
    }
    //Delete fixed id
    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideDto> getPassengersActiveRide(@PathVariable Long passengerId) {
        Ride ride = rideService.findByPassengerIdAndStatus(passengerId, Enums.RideStatus.ACTIVE.ordinal());
        if (ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<RideDto> getRideById(@PathVariable Long id){
        Ride ride = rideService.findById(id);
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    //Voznja moze da se prekine samo ukoliko je stanje voznje pending ili accepted,
    //Radi testiranja validacija stanja je zakomentarisana
    @PutMapping(value = "/{id}/withdraw")
    public ResponseEntity<RideDto> cancelRideById(@PathVariable Long id) throws RideNotFoundException{
        Ride ride = rideService.findById(id);
//        Ride ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.PENDING.ordinal());
        if (ride == null){
            throw new RideNotFoundException();
//            ride = ridesService.findByIdAndStatus(id, Enums.RideStatus.ACCEPTED.ordinal());
//            if(ride == null)
//                return new ResponseEntity<>("Ride does not exist", HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.REJECTED);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "/{rideId}/panic")
    public ResponseEntity<PanicDetailsDto> creatingPanicProcedure(@RequestBody PanicCreateDto reason, @PathVariable Long rideId){
        User user = passengerService.findById(3L);
        Ride ride = rideService.findById(rideId);
        Panic panic = new Panic(reason,ride,user);
        panicService.save(panic);
        return new ResponseEntity<>(new PanicDetailsDto(panic), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/accept")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long id){
        Ride ride = rideService.findById(id);
        if(ride == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        ride.setStatus(Enums.RideStatus.ACCEPTED);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/end")
    public ResponseEntity<RideDto> endRide(@PathVariable Long id) throws RideNotFoundException{
        Ride ride = rideService.findById(id);
        if(ride == null){
            throw new RideNotFoundException();
        }
        ride.setStatus(Enums.RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());  // ride finishes as soon as its status is set to finished
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping(value = "{id}/cancel")
    public ResponseEntity<RideDto> rejectRide(@PathVariable Long id, @Valid @RequestBody RideRejectDto rideReject) throws RideNotFoundException {
        Ride ride = rideService.findById(id);
        if(ride == null){
            throw new RideNotFoundException();
        }
        Refusal refusal = new Refusal(rideReject);
        refusal.setRide(ride);
        ride.setRefusal(refusal);
        ride.setStatus(Enums.RideStatus.CANCELED);  // Ovo je nekad bilo REJECTED ali sam stavio na CANCELED jer ima vise smisla
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<RideDto> startRide(@PathVariable Long id) throws RideNotFoundException {
        Ride ride = rideService.findById(id);
        if(ride == null){
            throw new RideNotFoundException();
        }
        ride.setStatus(Enums.RideStatus.ACTIVE);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }


    @PutMapping("/setDriver")
    public ResponseEntity<RideDto> setDriver(@RequestBody RideAddDriverDto rideAddDriverDto){
        Ride ride = rideService.findById(rideAddDriverDto.getRideId());
        Driver driver = driverService.findById(rideAddDriverDto.getDriverId());
        ride.setDriver(driver);
        rideService.save(ride);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }


    public Ride savePassengersAndDrivers(RideCreationDto rideRequestDto){
        Ride ride = new Ride(rideRequestDto);

        // setting vehicle type for the ride
        ride.setVehicleType(vehicleTypeService.getByName(rideRequestDto.getVehicleType()));

        // setting the price
        int totalDistance = 0;
        for (Route r : ride.getRoutes()) {
            totalDistance += r.getDistanceInMeters();
            System.err.println(r);
        }
        ride.setPrice(ride.getVehicleType().getPricePerKm() + totalDistance * 120);

        // adding passengers to ride
        for (UserRefDto passengerRef : rideRequestDto.getPassengers()){
                Passenger passenger = passengerService.findById(passengerRef.getId());
                if (passenger == null) {
                    continue;
                }
                passenger.getRides().add(ride);
                ride.getPassengers().add(passenger);
        }

        // adding driver to ride
        Driver driver = driverService.findById(2L);
        if (driver != null) {
            ride.setDriver(driver);
        }

        rideService.save(ride);

        return ride;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/scheduled/ride")
    public Map<String, Object> sendScheduledRide(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            this.simpMessagingTemplate.convertAndSend("/socket-scheduled-ride", socketMessageConverted);
        }

        return socketMessageConverted;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/ride/evaluation")
    public Map<String, Object> sendRide(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            this.simpMessagingTemplate.convertAndSend("/socket-ride-evaluation", socketMessageConverted);
        }

        return socketMessageConverted;
    }

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }


}
