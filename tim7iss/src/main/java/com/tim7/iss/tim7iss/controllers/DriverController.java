package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.*;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.responses.DriverWorkHoursResponse;
import com.tim7.iss.tim7iss.responses.PaginatedDriversResponse;
import com.tim7.iss.tim7iss.services.DocumentService;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.VehicleService;
import com.tim7.iss.tim7iss.services.WorkHourService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping
    public ResponseEntity<PaginatedDriversResponse> getAll(Pageable pageable) {
        Page<Driver> allDrivers = driverService.getAll(pageable);
        Collection<DriverDTO> driverDTOs = new ArrayList<>();
        allDrivers.forEach(driver -> driverDTOs.add(new DriverDTO(driver)));
        return new ResponseEntity<>(new PaginatedDriversResponse(driverService.countAll(), driverDTOs), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getById(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new DriverDTO(driver),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DriverDTO> save(@RequestBody Driver newDriver) {
        Driver driver = driverService.getByEmailAddress(newDriver.getEmailAddress());
        if (driver != null) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        driverService.save(newDriver);
        return new ResponseEntity<>(new DriverDTO(newDriver), HttpStatus.OK);
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
        Collection<Document> driverDocuments = driver.getDocuments();
//        Collection<Document> driverDocuments = documentService.getAllByDriverId(id);  // alternative way
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
        Vehicle vehicle = driver.getVehicle();
//        Vehicle vehicle = vehicleService.getByDriverId(id);  // alternative way
        if (vehicle == null) {
            return new ResponseEntity<>(new VehicleDTO(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new VehicleDTO(vehicle), HttpStatus.OK);
    }

    @PostMapping("{id}/vehicle")
    public ResponseEntity<VehicleDTO> addVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        System.err.println(id);
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if (driver.getVehicle() != null) {
            driver.getVehicle().setDriver(null);
            driverService.save(driver);
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
    public ResponseEntity<DriverWorkHoursResponse> getWorkHours(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Collection<WorkHour> workHours = driver.getWorkHours();
        List<WorkHourDTO> workHoursDTOs = new ArrayList<>();
        workHours.forEach(workHour -> workHoursDTOs.add(new WorkHourDTO(workHour)));
        return new ResponseEntity<>(new DriverWorkHoursResponse(workHoursDTOs.size(), workHoursDTOs), HttpStatus.OK);
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

    @GetMapping("{id}/ride")
    public ResponseEntity<Collection<RideDTO>> getRides(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Collection<Ride> rides = driver.getRides();
        List<RideDTO> rideDTOs = new ArrayList<>();
        rides.forEach(ride -> rideDTOs.add(new RideDTO(ride)));
        return new ResponseEntity<>(rideDTOs, HttpStatus.OK);
    }

    @GetMapping("{id}/working-hour/{workingHourId}")
    public ResponseEntity<WorkHourDTO> getWorkHourDetails(@PathVariable Long id, @PathVariable Long workingHourId) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Collection<WorkHour> workHours = driver.getWorkHours();
        List<WorkHour> filteredWorkHours = workHours.stream()
                .filter(workHour -> workHour.getId().equals(workingHourId))
                .toList();
        if (filteredWorkHours.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new WorkHourDTO(filteredWorkHours.get(0)), HttpStatus.OK);
    }

    @PutMapping("{id}/working-hour/{workingHourId}")
    public ResponseEntity<WorkHourDTO> changeWorkHour(@PathVariable Long id, @PathVariable Long workingHourId,
                                                      @RequestBody WorkHour newWorkHour) {
        Driver driver = driverService.getById(id);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Collection<WorkHour> workHours = driver.getWorkHours();
        List<WorkHour> filteredWorkHours = workHours.stream()
                .filter(workHour -> workHour.getId().equals(workingHourId))
                .toList();
        if (filteredWorkHours.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        WorkHour workHour = filteredWorkHours.get(0);
        workHour.setStartDate(newWorkHour.getStartDate());
        workHour.setEndDate(newWorkHour.getEndDate());
        workHourService.save(workHour);
        return new ResponseEntity<>(new WorkHourDTO(workHour), HttpStatus.OK);
    }

}
