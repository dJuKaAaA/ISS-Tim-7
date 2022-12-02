package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.DocumentDTO;
import com.tim7.iss.tim7iss.DTOs.DriverDTO;
import com.tim7.iss.tim7iss.DTOs.VehicleDTO;
import com.tim7.iss.tim7iss.models.Document;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.services.DocumentService;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("")
    public ResponseEntity<Collection<DriverDTO>> getAll() {
        Collection<Driver> allDrivers = driverService.getAll();
        Collection<DriverDTO> driverDTOs = new ArrayList<>();
        allDrivers.forEach(driver -> driverDTOs.add(new DriverDTO(driver)));
        return new ResponseEntity<>(
                driverDTOs,
                HttpStatus.OK
        );
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
        Collection<Document> driverDocuments = documentService.getDriverDocuments(id);
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
        Collection<Document> driverDocuments = documentService.getDriverDocuments(id);
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
    public ResponseEntity<VehicleDTO> getDriverVehicle(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Vehicle vehicle = vehicleService.getDriverVehicle(id);
        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }

}
