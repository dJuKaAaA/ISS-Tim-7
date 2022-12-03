package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.DocumentDTO;
import com.tim7.iss.tim7iss.DTOs.DriverDTO;
import com.tim7.iss.tim7iss.DTOs.VehicleDTO;
import com.tim7.iss.tim7iss.DTOs.WorkHourDTO;
import com.tim7.iss.tim7iss.models.Document;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.models.WorkHour;
import com.tim7.iss.tim7iss.services.DocumentService;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.VehicleService;
import com.tim7.iss.tim7iss.services.WorkHourService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Transactional
@RestController
@RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private WorkHourService workHourService;

    @GetMapping("")
    public ResponseEntity<Collection<DriverDTO>> getAll() {
        Collection<Driver> allDrivers = driverService.getAll();
        Collection<DriverDTO> driverDTOs = new ArrayList<>();
        allDrivers.forEach(driver -> driverDTOs.add(new DriverDTO(driver)));
        return new ResponseEntity<>(driverDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getById(@PathVariable Long id) throws RuntimeException {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new DriverDTO(driver),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    public ResponseEntity<DriverDTO> save(@RequestBody Driver driver) {
        driverService.save(driver);
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> save(@PathVariable Long id, @RequestBody Driver driver) {
        Driver oldDriver = driverService.getById(id);
        if (oldDriver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        driver.setId(id);
        driverService.save(driver);
        return new ResponseEntity<>(new DriverDTO(driver), HttpStatus.OK);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<Collection<DocumentDTO>> getDocuments(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
//        Collection<Document> driverDocuments = documentService.getAllByDriverId(id);
        Collection<Document> driverDocuments = driver.getDocuments();
        List<DocumentDTO> documentDTOs = new ArrayList<>();
        driverDocuments.forEach(document -> documentDTOs.add(new DocumentDTO(document)));
        return new ResponseEntity<>(documentDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/documents")
    public ResponseEntity<Boolean> deleteDocuments(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        Collection<Document> driverDocuments = documentService.getAllByDriverId(id);
        driverDocuments.forEach(document -> documentService.delete(document));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<DocumentDTO> addDocument(@PathVariable Long id, @RequestBody Document document) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        document.setDriver(driver);
        documentService.save(document);
        return new ResponseEntity<>(new DocumentDTO(document), HttpStatus.OK);
    }

    @GetMapping("{id}/vehicle")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Vehicle vehicle = vehicleService.getByDriverId(id);
        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }

    @PostMapping("{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        vehicle.setDriver(driver);
        vehicleService.save(vehicle);
        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }

    @PutMapping("{id}/vehicle")
    public ResponseEntity<VehicleDTO> changeVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        vehicle.setId(driver.getVehicle().getId());
        vehicle.setDriver(driver);
        vehicleService.save(vehicle);
        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }

    @GetMapping("{id}/working-hours")
    public ResponseEntity<Collection<WorkHourDTO>> getWorkHours(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Collection<WorkHour> workHours = driver.getWorkHours();
        List<WorkHourDTO> workHoursDTOs = new ArrayList<>();
        workHours.forEach(workHour -> workHoursDTOs.add(new WorkHourDTO(workHour)));
        return new ResponseEntity<>(workHoursDTOs, HttpStatus.OK);
    }


    @PostMapping("{id}/working-hours")
    public ResponseEntity<WorkHourDTO> addWorkHour(@PathVariable Long id, @RequestBody WorkHour workHour) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        workHour.getDrivers().add(driver);
        workHourService.save(workHour);
        return new ResponseEntity<>(new WorkHourDTO(workHour), HttpStatus.OK);
    }

}
