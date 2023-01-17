package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.GeoCoordinateDto;
import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.VehicleDto;
import com.tim7.iss.tim7iss.exceptions.VehicleNotAssignedException;
import com.tim7.iss.tim7iss.exceptions.VehicleNotFoundException;
import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.models.VehicleType;
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
    private VehicleService vehicleService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<VehicleDto> getById(@PathVariable Long id) {

        Vehicle vehicle = vehicleService.getById(id);

        // course must exist
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new VehicleDto(vehicle), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<VehicleDto>> getAll() {
        List<VehicleDto> vehicles = new ArrayList<>();
        for(Vehicle vehicle : vehicleService.getAll()){
            vehicles.add(new VehicleDto(vehicle));
        }
        return new ResponseEntity<>(new PaginatedResponseDto<>(vehicles.size(), vehicles), HttpStatus.OK);
    }

    @PutMapping("/{id}/location")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<String> changeLocation(@RequestHeader(value = "Authorization")String authHeader,@PathVariable Long id, @Valid @RequestBody GeoCoordinateDto location) throws VehicleNotAssignedException, VehicleNotFoundException {
        Vehicle vehicle = vehicleService.getById(id);
        if(vehicle == null){
            throw new VehicleNotFoundException("Vehicle does not exist!");
        }
        if(vehicle.getDriver() == null)
            throw new VehicleNotAssignedException("Vehicle is not assigned to the specific driver!");
        Location newLocation = new Location(location);
        newLocation.setId(vehicle.getLocation().getId());
        vehicle.setLocation(newLocation);
        vehicleService.save(vehicle);
        return new ResponseEntity<>("Cordinates successfully updated", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/driver/{driver_id}")
    public ResponseEntity<VehicleDto> setDriver(@PathVariable Long id, @PathVariable Long driver_id){
        Vehicle vehicle = vehicleService.getById(id);
        vehicle.setDriver(driverService.findById(driver_id));
        vehicleService.save(vehicle);
        return new ResponseEntity<>(new VehicleDto(vehicle), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VehicleDto> save(@Valid @RequestBody VehicleDto vehicleRequest){
        VehicleType vehicleType = vehicleTypeService.getByName(vehicleRequest.getVehicleType());
        Vehicle newVehicle = new Vehicle(vehicleRequest, vehicleType);
        vehicleService.save(newVehicle);
        return new ResponseEntity<>(new VehicleDto(newVehicle), HttpStatus.OK);
    }
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }
}
