package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.GeoCoordinateDto;
import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.VehicleDto;
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
    public ResponseEntity<String> changeLocation(@PathVariable Long id, @RequestBody GeoCoordinateDto location){
        Vehicle vehicle = vehicleService.getById(id);
        if(vehicle == null){
            return new ResponseEntity<>("Vehicle does not exist", HttpStatus.NOT_FOUND);
        }
        Location newLocation = new Location(location);
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
