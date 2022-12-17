package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.Member2.VehicleDTOs.VehicleDTO;
import com.tim7.iss.tim7iss.DTOs.apidriver.VehicleRequestBodyDTO;
import com.tim7.iss.tim7iss.DTOs.apidriver.VehicleResponseDTO;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationResponseDTO;
import com.tim7.iss.tim7iss.models.VehicleType;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.LocationService;
import com.tim7.iss.tim7iss.services.VehicleService;
import com.tim7.iss.tim7iss.services.VehicleTypeService;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {

        Vehicle vehicle = vehicleService.getById(id);

        // course must exist
        if (vehicle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAll() {
        List<Vehicle> vehicles = vehicleService.getAll();
        List<VehicleDTO> vehicleDTOs = new ArrayList<>();
        for(Vehicle vehicle : vehicles){
            vehicleDTOs.add(new VehicleDTO(vehicle));
        }
        return new ResponseEntity<>(vehicleDTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<String>changeLocation(@PathVariable Long id, @RequestBody LocationResponseDTO location){
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
    public ResponseEntity<VehicleDTO>setDriver(@PathVariable Long id, @PathVariable Long driver_id){
        Vehicle vehicle = vehicleService.getById(id);
        vehicle.setDriver(driverService.findById(driver_id));
        vehicleService.save(vehicle);
        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VehicleRequestBodyDTO>save(@RequestBody VehicleRequestBodyDTO vehicleRequest){
        VehicleType vehicleType = vehicleTypeService.getByName(vehicleRequest.getVehicleType());
        vehicleService.save(new Vehicle(vehicleRequest, vehicleType));
        return new ResponseEntity<>(vehicleRequest,HttpStatus.OK);
    }
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestException(){
//        return "Invalid data";
//    }
}
