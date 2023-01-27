package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.GeoCoordinateDto;
import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.VehicleDto;
import com.tim7.iss.tim7iss.exceptions.VehicleNotAssignedException;
import com.tim7.iss.tim7iss.exceptions.VehicleNotFoundException;
import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.models.VehicleType;
import com.tim7.iss.tim7iss.repositories.VehicleRepository;
import com.tim7.iss.tim7iss.repositories.VehicleTypeRepository;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.LocationService;
import com.tim7.iss.tim7iss.services.VehicleService;
import com.tim7.iss.tim7iss.services.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/vehicle")
@Transactional
@CrossOrigin
public class VehicleController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<VehicleDto> getById(@PathVariable Long id) {

        Vehicle vehicle = vehicleRepository.findById(id).get();  // TODO: Refactor (put the logic in the service class)

        // course must exist
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new VehicleDto(vehicle), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<VehicleDto>> getAll() {
        List<VehicleDto> vehicles = new ArrayList<>();
        for(Vehicle vehicle : vehicleRepository.findAll()){
            vehicles.add(new VehicleDto(vehicle));
        }
        return new ResponseEntity<>(new PaginatedResponseDto<>(vehicles.size(), vehicles), HttpStatus.OK);
    }

    @PutMapping("/{id}/location")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<String> changeLocation(@PathVariable Long id, @Valid @RequestBody GeoCoordinateDto location) throws VehicleNotAssignedException, VehicleNotFoundException {
        Vehicle vehicle = vehicleRepository.findById(id).get();  // TODO: Refactor (put the logic in the service class)
        if(vehicle == null){
            throw new VehicleNotFoundException("Vehicle does not exist!");
        }
        if(vehicle.getDriver() == null)
            throw new VehicleNotAssignedException("Vehicle is not assigned to the specific driver!");
        Location newLocation = new Location(location);
        newLocation.setId(vehicle.getLocation().getId());
        vehicle.setLocation(newLocation);
        vehicleRepository.save(vehicle);
        return new ResponseEntity<>("Coordinates successfully updated", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/driver/{driver_id}")
    public ResponseEntity<VehicleDto> setDriver(@PathVariable Long id, @PathVariable Long driver_id){
        Vehicle vehicle = vehicleRepository.findById(id).get();  // TODO: Refactor (put the logic in the service class)
        vehicle.setDriver(driverService.findById(driver_id));
        vehicleRepository.save(vehicle);
        return new ResponseEntity<>(new VehicleDto(vehicle), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VehicleDto> save(@Valid @RequestBody VehicleDto vehicleRequest){
        VehicleType vehicleType = vehicleTypeRepository.findByName(vehicleRequest.getVehicleType()).get();
        Vehicle newVehicle = new Vehicle(vehicleRequest, vehicleType);
        vehicleRepository.save(newVehicle);
        return new ResponseEntity<>(new VehicleDto(newVehicle), HttpStatus.OK);
    }
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }
}
