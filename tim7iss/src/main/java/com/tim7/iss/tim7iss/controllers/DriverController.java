package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private LocationService locationService;

    @Autowired
    private RideService rideService;

    @Autowired
    RequestService requestService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<UserDto>> getAll(Pageable pageable) {
        Collection<UserDto> drivers = driverService.getAll(pageable)
                .stream()
                .map(UserDto::new)
                .toList();
        return new ResponseEntity<>(new PaginatedResponseDto<>(drivers.size(), drivers), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        return new ResponseEntity<>(new UserDto(driver), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto driverRequestBodyDto) throws EmailAlreadyExistsException {
        Optional<Driver> driverByEmail = driverService.getByEmailAddress(driverRequestBodyDto.getEmail());
        if (driverByEmail.isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        Driver newDriver = new Driver(driverRequestBodyDto);
        String encodedPassword = bCryptPasswordEncoder.encode(driverRequestBodyDto.getPassword());
        newDriver.setPassword(encodedPassword);
        newDriver.setRoles(List.of(roleService.getRoleByName("ROLE_DRIVER")));
        driverService.save(newDriver);
        return new ResponseEntity<>(new UserDto(newDriver), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> save(@PathVariable Long id, @Valid @RequestBody UserDto driverRequestBodyDto) {
        Driver updatedDriver = new Driver(driverRequestBodyDto);
        updatedDriver.setId(id);
        driverService.save(updatedDriver);
        return new ResponseEntity<>(new UserDto(updatedDriver), HttpStatus.OK);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<Collection<DriverDocumentDto>> getDocuments(@PathVariable Long id) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        Collection<DriverDocumentDto> documents = driver.getDocuments()
                .stream()
                .map(DriverDocumentDto::new)
                .toList();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @DeleteMapping("/document/{documentId}")
    public HttpStatus deleteDocuments(@PathVariable Long documentId) throws DocumentNotFoundException {
        Document document = documentService.getById(documentId).orElseThrow(DocumentNotFoundException::new);
        documentService.delete(document);
        return HttpStatus.NO_CONTENT;
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<DriverDocumentDto> addDocument(@PathVariable Long id,
                                                         @Valid @RequestBody DriverDocumentDto driverDocumentDto) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        Document newDocument = new Document(driverDocumentDto, driver);
        documentService.save(newDocument);
        return new ResponseEntity<>(new DriverDocumentDto(newDocument), HttpStatus.OK);
    }

    @GetMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        // TODO: Throw driver doesn't have vehicle registered if vehicle is null
        Vehicle vehicle = driver.getVehicle();
        return new ResponseEntity<>(new VehicleDto(vehicle), HttpStatus.OK);
    }

    @PostMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDto> addVehicle(@PathVariable Long id,
                                                 @Valid @RequestBody VehicleDto vehicleRequestBodyDto) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        // TODO: Throw driver doesn't have vehicle registered if vehicle is null
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
                                                    @Valid @RequestBody VehicleDto vehicleRequestBodyDto) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        // TODO: Throw driver doesn't have vehicle registered if vehicle is null
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
    public ResponseEntity<PaginatedResponseDto<WorkingHourDto>> getWorkHours(@PathVariable Long id, Pageable page) throws UserNotFoundException {
        driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        Collection<WorkHour> paginatedWorkHours = workHourService.getByDriverId(id, page);
        List<WorkingHourDto> workHours = paginatedWorkHours.stream().map(WorkingHourDto::new).toList();
        return new ResponseEntity<>(new PaginatedResponseDto<>(workHours.size(), workHours), HttpStatus.OK);
    }

    @PostMapping("/{id}/working-hour")
    public ResponseEntity<WorkingHourDto> addWorkHour(@PathVariable Long id) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        WorkHour newWorkHour = new WorkHour();
        // TODO: Fetch start and end date from body
        newWorkHour.setStartDate(LocalDateTime.now());
        newWorkHour.setEndDate(LocalDateTime.now());
        newWorkHour.setDriver(driver);
        workHourService.save(newWorkHour);
        return new ResponseEntity<>(new WorkingHourDto(newWorkHour), HttpStatus.OK);
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(@PathVariable Long id, Pageable page) throws UserNotFoundException {
        driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        List<RideDto> rides = rideService.getByDriverId(id, page).stream().map(RideDto::new).toList();
        return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);
    }

    @GetMapping("/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHourDto> getWorkHourDetails(@PathVariable Long workingHourId) throws WorkHourNotFoundException {
        WorkHour workHour = workHourService.getById(workingHourId).orElseThrow(WorkHourNotFoundException::new);
        return new ResponseEntity<>(new WorkingHourDto(workHour), HttpStatus.OK);
    }

    @PutMapping("/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHourDto> changeWorkHour(@PathVariable Long workingHourId) throws WorkHourNotFoundException {
        WorkHour workHour = workHourService.getById(workingHourId).orElseThrow(WorkHourNotFoundException::new);
        WorkHour updatedWorkHour = new WorkHour();
        // TODO: Fetch start and end date from body
        updatedWorkHour.setStartDate(LocalDateTime.now());
        updatedWorkHour.setEndDate(LocalDateTime.now());
        updatedWorkHour.setId(workHour.getId());
        updatedWorkHour.setDriver(workHour.getDriver());
        workHourService.save(updatedWorkHour);
        return new ResponseEntity<>(new WorkingHourDto(updatedWorkHour), HttpStatus.OK);
    }

    @PostMapping("/request/{driverId}")
    public HttpStatus saveRequest(@PathVariable Long driverId,
                                  @RequestBody DriverChangeProfileRequestDto requestDto) {
        return requestService.saveRequest(driverId,requestDto);
    }

    @GetMapping("{id}/activity")
public ResponseEntity<ActivityDto> fetchActivity(@PathVariable Long id) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        return new ResponseEntity<>(new ActivityDto(driver.isActive()), HttpStatus.OK);
    }

    @PutMapping("/{id}/activity")
    public HttpStatus changeActivity(@PathVariable Long id, @Valid @RequestBody ActivityDto activity) throws UserNotFoundException {
        Driver driver = driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        driver.setActive(activity.getIsActive());
        driverService.save(driver);
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping("/{id}/rides/scheduled")
    public ResponseEntity<Collection<RideDto>> getPendingRides(@PathVariable Long id) throws UserNotFoundException {
        driverService.getById(id).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        Collection<RideDto> pendingRides = rideService.findByDriverIdAndStatus(id, Enums.RideStatus.PENDING.ordinal())
                .stream()
                .map(RideDto::new)
                .toList();
        Collection<RideDto> acceptedRides = rideService.findByDriverIdAndStatus(id, Enums.RideStatus.ACCEPTED.ordinal())
                .stream()
                .map((Ride ride) -> {
                    ride.setRoutes(ride.getRoutes().stream().sorted(Comparator.comparing(Route::getId)).toList());
                    return new RideDto(ride);
                })
                .toList();
        Collection<RideDto> rides = new ArrayList<>();
        rides.addAll(pendingRides);
        rides.addAll(acceptedRides);
        rides = rides
                .stream()
                .sorted(Comparator.comparing((RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat)))
                .toList();
        return new ResponseEntity<>(rides, HttpStatus.OK);
    }

    @GetMapping("/activity-and-locations")
    public ResponseEntity<PaginatedResponseDto<DriverActivityAndLocationDto>> fetchActivityAndLocations(Pageable pageable) {
        Collection<DriverActivityAndLocationDto> drivers = driverService.getAll(pageable)
                .stream()
                .map(DriverActivityAndLocationDto::new)
                .toList();
        return new ResponseEntity<>(new PaginatedResponseDto<>(drivers.size(), drivers), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/driver/send/current/location")
    public Map<String, Object> sendCurrentLocation(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            if (socketMessageConverted.containsKey("rideId") && socketMessageConverted.get("rideId") != null) {
                this.simpMessagingTemplate.convertAndSend("/socket-driver-movement/" + socketMessageConverted.get("rideId"),
                        socketMessageConverted);
            }
        }

        return socketMessageConverted;
    }


}
