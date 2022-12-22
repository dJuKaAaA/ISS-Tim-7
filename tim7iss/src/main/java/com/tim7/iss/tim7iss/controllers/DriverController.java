package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.apidriver.*;
import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.DTOs.apidriver.PaginatedDriverWorkHoursResponseDTO;
import com.tim7.iss.tim7iss.DTOs.apidriver.PaginatedDriversResponseDTO;
import com.tim7.iss.tim7iss.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Transactional
@RestController
@RequestMapping("api/driver")
@CrossOrigin
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private WorkHourService workHourService;

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RideService rideService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<UserDto>> getAll(Pageable pageable) {
        Page<Driver> allDrivers = driverService.getAll(pageable);
        Collection<UserDto> drivers = new ArrayList<>();
        allDrivers.forEach(driver -> drivers.add(new UserDto(driver)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(driverService.countAll(), drivers), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        return new ResponseEntity<>(new UserDto(driver), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto driverRequestBodyDto) {
        Driver newDriver = new Driver(driverRequestBodyDto);
        driverService.save(newDriver);
        return new ResponseEntity<>(new UserDto(newDriver), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> save(@PathVariable Long id, @RequestBody UserDto driverRequestBodyDto) {
        Driver updatedDriver = new Driver(driverRequestBodyDto);
        updatedDriver.setId(id);
        driverService.save(updatedDriver);
        return new ResponseEntity<>(new UserDto(updatedDriver), HttpStatus.OK);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<Collection<DriverDocumentDto>> getDocuments(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        Collection<DriverDocumentDto> documents = new ArrayList<>();
        driver.getDocuments().forEach(document -> documents.add(new DriverDocumentDto(document)));
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<Boolean> deleteDocuments(@PathVariable Long documentId) {
        documentService.deleteById(documentId);
        return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<DriverDocumentDto> addDocument(@PathVariable Long id,
                                                           @RequestBody DriverDocumentDto documentRequestBodyDto) {
        Driver driver = driverService.getById(id);
        Document newDocument = new Document(documentRequestBodyDto, driver);
        documentService.save(newDocument);
        return new ResponseEntity<>(new DriverDocumentDto(newDocument), HttpStatus.OK);
    }

    @GetMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        Vehicle vehicle = driver.getVehicle();
        return new ResponseEntity<>(new VehicleDto(vehicle), HttpStatus.OK);
    }

    @PostMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDto> addVehicle(@PathVariable Long id,
                                                         @RequestBody VehicleDto vehicleRequestBodyDto) {
        Driver driver = driverService.getById(id);
        if (driver.getVehicle() != null) {
            driver.getVehicle().setDriver(null);
            driverService.save(driver);
        }
        assert driver.getVehicle() != null;
        VehicleType vehicleType = driver.getVehicle().getVehicleType();

        // TODO: Fetch location from database
//        Location location = locationService.getByLongitudeAndLatitude(
//                vehicleRequestBodyDTO.getCurrentLocation().getLongitude(),
//                vehicleRequestBodyDTO.getCurrentLocation().getLatitude());
        Location location = locationService.getById(1L);
        Vehicle newVehicle = new Vehicle(vehicleRequestBodyDto, vehicleType, driver, location);
        vehicleService.save(newVehicle);
        return new ResponseEntity<>(new VehicleDto(newVehicle), HttpStatus.OK);
    }

    @PutMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDto> changeVehicle(@PathVariable Long id,
                                                            @RequestBody VehicleDto vehicleRequestBodyDto) {
        Driver driver = driverService.getById(id);
        if (driver.getVehicle() != null) {
            driver.getVehicle().setDriver(null);
            driverService.save(driver);
        }
        assert driver.getVehicle() != null;
        VehicleType vehicleType = driver.getVehicle().getVehicleType();

        // TODO: Fetch location from database
//        Location location = locationService.getByLongitudeAndLatitude(
//                vehicleRequestBodyDTO.getCurrentLocation().getLongitude(),
//                vehicleRequestBodyDTO.getCurrentLocation().getLatitude());
        Location location = locationService.getById(1L);
        Vehicle newVehicle = new Vehicle(vehicleRequestBodyDto, vehicleType, driver, location);
        newVehicle.setId(driver.getVehicle().getId());
        driver.setVehicle(newVehicle);
        driverService.save(driver);
        return new ResponseEntity<>(new VehicleDto(newVehicle), HttpStatus.OK);
    }

    @GetMapping("/{id}/working-hour")
    public ResponseEntity<PaginatedResponseDto<WorkingHourDto>> getWorkHours(@PathVariable Long id, Pageable page) {
        Collection<WorkHour> paginatedWorkHours = workHourService.getByDriverId(id, page);
        List<WorkingHourDto> workHours = new ArrayList<>();
        paginatedWorkHours.forEach(workHour -> workHours.add(new WorkingHourDto(workHour)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(workHourService.countAll(), workHours), HttpStatus.OK);
    }


    @PostMapping("/{id}/working-hour")
    public ResponseEntity<WorkingHourDto> addWorkHour(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        WorkHour newWorkHour = new WorkHour();
        newWorkHour.setStartDate(LocalDateTime.now());
        newWorkHour.setEndDate(LocalDateTime.now());
        newWorkHour.setDriver(driver);
        workHourService.save(newWorkHour);
        return new ResponseEntity<>(new WorkingHourDto(newWorkHour), HttpStatus.OK);
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(@PathVariable Long id, Pageable page) {
        List<RideDto> rides = new ArrayList<>();
        rideService.getByDriverId(id, page).forEach(ride -> rides.add(new RideDto(ride)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(rideService.countAll(), rides), HttpStatus.OK);
    }

    @GetMapping("/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHourDto> getWorkHourDetails(@PathVariable Long workingHourId) {
        WorkHour workHour = workHourService.getById(workingHourId);
        return new ResponseEntity<>(new WorkingHourDto(workHour), HttpStatus.OK);
    }

    @PutMapping("/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHourDto> changeWorkHour(@PathVariable Long workingHourId) {
        WorkHour workHour = workHourService.getById(workingHourId);
        WorkHour updatedWorkHour = new WorkHour();
        updatedWorkHour.setStartDate(LocalDateTime.now());
        updatedWorkHour.setEndDate(LocalDateTime.now());
        updatedWorkHour.setId(workHour.getId());
        updatedWorkHour.setDriver(workHour.getDriver());
        workHourService.save(updatedWorkHour);
        return new ResponseEntity<>(new WorkingHourDto(updatedWorkHour), HttpStatus.OK);
    }

}
