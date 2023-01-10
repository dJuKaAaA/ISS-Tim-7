package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
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
import java.util.*;

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
    MapService mapService;

    @Autowired
    FavoriteLocationService favoriteLocationService;

    @Autowired
    LocationService locationService;

    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ResponseEntity<RideDto> save(@Valid @RequestBody RideCreationDto rideRequestDto) throws RideAlreadyPendingException {
        if(AnyRidesArePending())
            throw new RideAlreadyPendingException();
        Ride ride = savePassengersAndDrivers(rideRequestDto);
        Driver driver = driverService.findById(2L);
        float startLatitude = rideRequestDto.getLocations().get(0).getDeparture().getLatitude();
        float startLongitude = rideRequestDto.getLocations().get(0).getDeparture().getLongitude();
        float endLatitude = driver.getVehicle().getLocation().getLatitude();
        float endLongitude = driver.getVehicle().getLocation().getLongitude();
        Integer distance = mapService.getDistance(startLatitude, startLongitude, endLatitude, endLongitude);
        return new ResponseEntity<>(new RideDto(ride), HttpStatus.OK);
    }

    private boolean AnyRidesArePending() {
        Ride ride = rideService.findByStatus(Enums.RideStatus.PENDING);
        if(ride == null)
            return false;
        return true;
    }

    @PostMapping(value = "/favorites")
    public ResponseEntity<FavoriteLocationDto>createFavoriteLocation(@RequestBody FavoriteLocationDto favoriteLocationDto) throws TooManyFavoriteRidesException {
        //privremen id passengera koji salje request
        Long passengerId = 4L;
        if(checkIfMoreThan9FavoriteLocations(passengerId)){
            throw new TooManyFavoriteRidesException();
        }
        Set<Passenger>passengers = new HashSet<>();
        for(UserRefDto pass: favoriteLocationDto.getPassengers()){
            passengers.add(passengerService.findById(pass.getId()));
        }
        VehicleType vehicleType = vehicleTypeService.getByName(favoriteLocationDto.getVehicleType());
        FavoriteLocation favoriteLocation = new FavoriteLocation(favoriteLocationDto, passengers, vehicleType);
        favoriteLocation = favoriteLocationService.save(favoriteLocation);
        favoriteLocationDto.setId(favoriteLocation.getId());
        return new ResponseEntity<>(favoriteLocationDto, HttpStatus.OK);
    }

    @GetMapping(value = "/favorites")
    public ResponseEntity<List<FavoriteLocationDto>>getFavoriteLocations(){
        List<FavoriteLocation>favoriteLocations = favoriteLocationService.getAll();
        List<FavoriteLocationDto>favoriteLocationsDto = new ArrayList<>();
        for(FavoriteLocation favoriteLocation : favoriteLocations){
            favoriteLocationsDto.add(new FavoriteLocationDto(favoriteLocation));
        }
        return new ResponseEntity<>(favoriteLocationsDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/favorites/{id}")
    public ResponseEntity<String>deleteFavoriteLocation(@PathVariable Long id) throws FavoriteLocationNotFoundException {
        if(favoriteLocationService.findById(id) == null)
            throw new FavoriteLocationNotFoundException();
        favoriteLocationService.delete(id);
        return new ResponseEntity("Successful deletion of favorite location!", HttpStatus.OK);
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
    public ResponseEntity<PanicDetailsDto> creatingPanicProcedure(@Valid @RequestBody PanicCreateDto reason, @PathVariable Long rideId){
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
    public ResponseEntity<RideDto> setDriver(@Valid @RequestBody RideAddDriverDto rideAddDriverDto){
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

    public boolean checkIfMoreThan9FavoriteLocations(Long id){
        List<FavoriteLocation> locations = favoriteLocationService.findByPassengerId(id);
        if(locations.size() > 9)
            return true;
        return false;
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
