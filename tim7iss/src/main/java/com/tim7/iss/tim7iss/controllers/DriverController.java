package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.apidriver.*;
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
    public ResponseEntity<PaginatedDriversResponseDTO> getAll(Pageable pageable) {
        Page<Driver> allDrivers = driverService.getAll(pageable);
        Collection<DriverResponseDTO> driverDTOs = new ArrayList<>();
        allDrivers.forEach(driver -> driverDTOs.add(new DriverResponseDTO(driver)));
        return new ResponseEntity<>(new PaginatedDriversResponseDTO(driverService.countAll(), driverDTOs), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getById(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        return new ResponseEntity<>(
                new DriverResponseDTO(driver),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DriverResponseDTO> save(@RequestBody DriverRequestBodyDTO driverRequestBodyDTO) {
        Driver newDriver = new Driver(driverRequestBodyDTO);
        driverService.save(newDriver);
        return new ResponseEntity<>(new DriverResponseDTO(newDriver), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> save(@PathVariable Long id,
                                                  @RequestBody DriverRequestBodyDTO driverRequestBodyDTO) {
        Driver updatedDriver = new Driver(driverRequestBodyDTO);
        updatedDriver.setId(id);
        driverService.save(updatedDriver);
        return new ResponseEntity<>(new DriverResponseDTO(updatedDriver), HttpStatus.OK);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<Collection<DocumentResponseDTO>> getDocuments(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        Collection<Document> driverDocuments = driver.getDocuments();
//        Collection<Document> driverDocuments = documentService.getAllByDriverId(id);  // alternative way
        List<DocumentResponseDTO> documentDTOs = new ArrayList<>();
        driverDocuments.forEach(document -> documentDTOs.add(new DocumentResponseDTO(document)));
        return new ResponseEntity<>(documentDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<Boolean> deleteDocuments(@PathVariable Long documentId) {
        documentService.deleteById(documentId);
        return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<DocumentResponseDTO> addDocument(@PathVariable Long id,
                                                           @RequestBody DocumentRequestBodyDTO documentRequestBodyDTO) {
        Driver driver = driverService.getById(id);
        Document newDocument = new Document(documentRequestBodyDTO, driver);
        documentService.save(newDocument);
        return new ResponseEntity<>(new DocumentResponseDTO(newDocument), HttpStatus.OK);
    }

    @GetMapping("{id}/vehicle")
    public ResponseEntity<VehicleResponseDTO> getVehicle(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        Vehicle vehicle = driver.getVehicle();
//        Vehicle vehicle = vehicleService.getByDriverId(id);  // alternative way
        return new ResponseEntity<>(new VehicleResponseDTO(vehicle), HttpStatus.OK);
    }

    @PostMapping("{id}/vehicle")
    public ResponseEntity<VehicleResponseDTO> addVehicle(@PathVariable Long id,
                                                         @RequestBody VehicleRequestBodyDTO vehicleRequestBodyDTO) {
        Driver driver = driverService.getById(id);
        if (driver.getVehicle() != null) {
            driver.getVehicle().setDriver(null);
            driverService.save(driver);
        }
        VehicleType vehicleType = vehicleTypeService.getByName(vehicleRequestBodyDTO.getVehicleType());

        // TODO: Fetch location from database
//        Location location = locationService.getByLongitudeAndLatitude(
//                vehicleRequestBodyDTO.getCurrentLocation().getLongitude(),
//                vehicleRequestBodyDTO.getCurrentLocation().getLatitude());
        Location location = locationService.getById(1L);
        Vehicle newVehicle = new Vehicle(vehicleRequestBodyDTO, vehicleType, driver, location);
        vehicleService.save(newVehicle);
        return new ResponseEntity<>(new VehicleResponseDTO(newVehicle), HttpStatus.OK);
    }

    @PutMapping("{id}/vehicle")
    public ResponseEntity<VehicleResponseDTO> changeVehicle(@PathVariable Long id,
                                                            @RequestBody VehicleRequestBodyDTO vehicleRequestBodyDTO) {
        Driver driver = driverService.getById(id);
        if (driver.getVehicle() != null) {
            driver.getVehicle().setDriver(null);
            driverService.save(driver);
        }
        VehicleType vehicleType = vehicleTypeService.getByName(vehicleRequestBodyDTO.getVehicleType());

        // TODO: Fetch location from database
//        Location location = locationService.getByLongitudeAndLatitude(
//                vehicleRequestBodyDTO.getCurrentLocation().getLongitude(),
//                vehicleRequestBodyDTO.getCurrentLocation().getLatitude());
        Location location = locationService.getById(1L);
        Vehicle newVehicle = new Vehicle(vehicleRequestBodyDTO, vehicleType, driver, location);
        newVehicle.setId(driver.getVehicle().getId());
        vehicleService.save(newVehicle);
        return new ResponseEntity<>(new VehicleResponseDTO(newVehicle), HttpStatus.OK);
    }

    @GetMapping("{id}/working-hour")
    public ResponseEntity<PaginatedDriverWorkHoursResponseDTO> getWorkHours(@PathVariable Long id, Pageable page) {
        Driver driver = driverService.getById(id);
        Collection<WorkHour> workHours = workHourService.getByDriverId(id, page);
        List<WorkHourResponseDTO> workHoursDTOs = new ArrayList<>();
        workHours.forEach(workHour -> workHoursDTOs.add(new WorkHourResponseDTO(workHour)));
        return new ResponseEntity<>(new PaginatedDriverWorkHoursResponseDTO(workHourService.countAll(),
                workHoursDTOs), HttpStatus.OK);
    }


    @PostMapping("{id}/working-hour")
    public ResponseEntity<WorkHourResponseDTO> addWorkHour(@PathVariable Long id) {
        WorkHourRequestBodyDTO workHourRequestBodyDTO = new WorkHourRequestBodyDTO(
                LocalDateTime.now(), LocalDateTime.now());
        Driver driver = driverService.getById(id);
        WorkHour newWorkHour = new WorkHour(workHourRequestBodyDTO);
        newWorkHour.setDriver(driver);
        workHourService.save(newWorkHour);
        return new ResponseEntity<>(new WorkHourResponseDTO(newWorkHour), HttpStatus.OK);
    }

    @GetMapping("{id}/ride")
    public ResponseEntity<PaginatedDriverRidesResponseDTO> getRides(@PathVariable Long id, Pageable page) {
        Collection<Ride> rides = rideService.getByDriverId(id, page);
        List<RideResponseDTO> rideDTOs = new ArrayList<>();
        rides.forEach(ride -> rideDTOs.add(new RideResponseDTO(ride)));
        return new ResponseEntity<>(new PaginatedDriverRidesResponseDTO(rideService.countAll(), rideDTOs), HttpStatus.OK);
    }

    @GetMapping("/working-hour/{workingHourId}")
    public ResponseEntity<WorkHourResponseDTO> getWorkHourDetails(@PathVariable Long workingHourId) {
        WorkHour workHour = workHourService.getById(workingHourId);
        return new ResponseEntity<>(new WorkHourResponseDTO(workHour), HttpStatus.OK);
    }

    @PutMapping("working-hour/{workingHourId}")
    public ResponseEntity<WorkHourResponseDTO> changeWorkHour(@PathVariable Long workingHourId) {
        WorkHourRequestBodyDTO workHourRequestBodyDTO = new WorkHourRequestBodyDTO(
                LocalDateTime.of(2022, 11, 7, 21, 0),
                LocalDateTime.of(2022, 11, 7, 22, 0));
        WorkHour workHour = workHourService.getById(workingHourId);
        WorkHour newWorkHour = new WorkHour(workHourRequestBodyDTO);
        newWorkHour.setId(workHour.getId());
        newWorkHour.setDriver(workHour.getDriver());
        workHourService.save(newWorkHour);
        return new ResponseEntity<>(new WorkHourResponseDTO(newWorkHour), HttpStatus.OK);
    }

}
