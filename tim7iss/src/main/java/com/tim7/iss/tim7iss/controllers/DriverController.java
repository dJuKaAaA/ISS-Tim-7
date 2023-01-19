package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.services.*;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Transactional
@RestController
@RequestMapping("api/driver")
@CrossOrigin
public class DriverController {

    @Autowired
    RequestService requestService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private DriverRepository driverRepository;
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
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ImageService imageService;

    @Autowired
    private TokenUtils tokenUtils;

    //-------------------------------------------------------------------------------------------------------------
    //--------------------- Endpoints according to swagger specification -----------------------------------------
    //-------------------------------------------------------------------------------------------------------------

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PaginatedResponseDto<UserDto>> getAll(Pageable page) {
        PaginatedResponseDto<UserDto> paginatedDrivers = driverService.getPaginatedDriversAsDto(page);
        return new ResponseEntity<>(paginatedDrivers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) throws DriverNotFoundException {
        UserDto driverDto = driverService.getDriverDtoById(id);
        return new ResponseEntity<>(driverDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto driverRequestBodyDto)
            throws EmailAlreadyExistsException, NotAnImageException, LargeImageException {
        Driver newDriver = driverService.createAccount(driverRequestBodyDto);
        return new ResponseEntity<>(new UserDto(newDriver), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> save(@PathVariable Long id, @Valid @RequestBody UserDto driverChanges)
            throws DriverNotFoundException, NotAnImageException, LargeImageException {
        Driver updatedDriver = driverService.makeChangesToAccount(id, driverChanges);
        return new ResponseEntity<>(new UserDto(updatedDriver), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @GetMapping("/{id}/documents")
    public ResponseEntity<Collection<DriverDocumentDto>> getDocuments(@PathVariable Long id)
            throws DriverNotFoundException {
        Collection<DriverDocumentDto> documentsDto = driverService.getDriverDocumentsAsDto(id);
        return new ResponseEntity<>(documentsDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<String> deleteDocuments(@PathVariable Long documentId) throws DocumentNotFoundException {
        documentService.deleteById(documentId);
        return new ResponseEntity<>("Driver document deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/documents")
    public ResponseEntity<DriverDocumentDto> addDocument(@PathVariable Long id,
                                                         @Valid @RequestBody DriverDocumentDto driverDocumentDto)
            throws DriverNotFoundException, NotAnImageException, LargeImageException {
        Document newDocument = documentService.createDocumentForDriver(driverDocumentDto, id);
        return new ResponseEntity<>(new DriverDocumentDto(newDocument), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    @GetMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id)
            throws DriverNotFoundException, VehicleNotAssignedException {
        VehicleDto assignedVehicleDto = driverService.getAssignedVehicle(id);
        return new ResponseEntity<>(assignedVehicleDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDto> addVehicle(@PathVariable Long id,
                                                 @Valid @RequestBody VehicleDto vehicleRequestBodyDto)
            throws DriverNotFoundException, VehicleAlreadyAssignedException {
        Vehicle newVehicle = vehicleService.createAndAssignToDriver(vehicleRequestBodyDto, id);
        return new ResponseEntity<>(new VehicleDto(newVehicle), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/vehicle")
    public ResponseEntity<VehicleDto> changeVehicle(@PathVariable Long id,
                                                    @Valid @RequestBody VehicleDto vehicleRequestBodyDto)
            throws DriverNotFoundException, VehicleNotAssignedException {
        Vehicle updatedVehicle = vehicleService.makeChanges(vehicleRequestBodyDto, id);
        return new ResponseEntity<>(new VehicleDto(updatedVehicle), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @GetMapping("/{id}/working-hour")
    public ResponseEntity<PaginatedResponseDto<WorkingHourDto>> getWorkHours(@PathVariable Long id, Pageable page)
            throws DriverNotFoundException {
        PaginatedResponseDto<WorkingHourDto> paginatedWorkHours = driverService.getPaginatedWorkingHoursAsDto(id, page);
        return new ResponseEntity<>(paginatedWorkHours, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @PostMapping("/{id}/working-hour")
    public ResponseEntity<WorkingHourDto> addWorkHour(@PathVariable Long id,
                                                      @Valid @RequestBody StartShiftDto startShiftDto)
            throws DriverNotFoundException, VehicleNotAssignedException, ShiftAlreadyOngoingException,
            ShiftExceededException {
        WorkHour shiftStart = workHourService.startShiftForDriver(startShiftDto, id);
        return new ResponseEntity<>(new WorkingHourDto(shiftStart), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @GetMapping("/{id}/ride")
    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(@PathVariable Long id, Pageable page)
            throws DriverNotFoundException {
        PaginatedResponseDto<RideDto> paginatedRides = rideService.getPaginatedRidesForDriverAsDto(id, page);
        return new ResponseEntity<>(paginatedRides, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @GetMapping("/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHourDto> getWorkHourDetails(@PathVariable Long workingHourId)
            throws WorkHourNotFoundException {
        WorkHour workHour = workHourService.getById(workingHourId);
        return new ResponseEntity<>(new WorkingHourDto(workHour), HttpStatus.OK);
    }

    /* Admin won't be able to access this endpoint even though he has authorization because this method uses
    *  the value from the token to identify the driver so it can fetch their ongoing shift */
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @PutMapping("/working-hour/{workingHourId}")
    public ResponseEntity<WorkingHourDto> changeWorkHour(@PathVariable Long workingHourId,
                                                         @Valid @RequestBody EndShiftDto endShiftDto,
                                                         Principal principal)
            throws NoShiftOngoingException, DriverNotFoundException, VehicleNotAssignedException {
        WorkHour completedShift = workHourService.endShiftForDriver(endShiftDto, principal.getName());
        return new ResponseEntity<>(new WorkingHourDto(completedShift), HttpStatus.OK);
    }

    //-------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @PutMapping("/{id}/working-hour")
    public ResponseEntity<WorkingHourDto> endShift(@PathVariable Long id,
                                                   @Valid @RequestBody EndShiftDto endShiftDto)
            throws DriverNotFoundException, VehicleNotAssignedException, NoShiftOngoingException {
        WorkHour completedShift = workHourService.endShiftForDriver(endShiftDto, id);
        return new ResponseEntity<>(new WorkingHourDto(completedShift), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN')")
    @PostMapping("/request/{driverId}")
    public HttpStatus saveRequest(@PathVariable Long driverId,
                                  @RequestBody DriverChangeProfileRequestDto requestDto) {
        return requestService.saveRequest(driverId, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @GetMapping("{id}/activity")
    public ResponseEntity<ActivityDto> fetchActivity(@PathVariable Long id) throws DriverNotFoundException {
        ActivityDto isActiveDto = driverService.getIsActiveAsDto(id);
        return new ResponseEntity<>(isActiveDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @PutMapping("/{id}/activity")
    public ResponseEntity<ActivityDto> changeActivity(@PathVariable Long id, @Valid @RequestBody ActivityDto activity)
            throws DriverNotFoundException {
        driverService.setActivityState(activity, id);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @GetMapping("/{id}/rides/scheduled")
    public ResponseEntity<Collection<RideDto>> getPendingRides(@PathVariable Long id) throws DriverNotFoundException {
        Collection<RideDto> scheduledRides = rideService.getScheduledRidesForDriverAsDto(id);
        return new ResponseEntity<>(scheduledRides, HttpStatus.OK);
    }

    @GetMapping("/locations")
    public ResponseEntity<PaginatedResponseDto<DriverLocationDto>> fetchActivityAndLocations(Pageable page) {
        PaginatedResponseDto<DriverLocationDto> paginatedDriverLocations = driverService
                .getPaginatedLocationsAsDto(page);
        return new ResponseEntity<>(paginatedDriverLocations, HttpStatus.OK);
    }

    // web socket endpoint for receiving and then sending current driver location to subscribers
    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/driver/send/current/location")
    public Map<String, Object> sendCurrentLocation(String socketMessage) {
        Map<String, Object> socketMessageConverted = Constants.parseJsonString(socketMessage);

        if (socketMessageConverted != null) {
            if (socketMessageConverted.containsKey("rideId") && socketMessageConverted.get("rideId") != null) {
                this.simpMessagingTemplate.convertAndSend("/socket-driver-movement/to-ride/" + socketMessageConverted.get("rideId"),
                        socketMessageConverted);
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> passengers = (List<Map<String, Object>>) socketMessageConverted.get("passengers");
                for (Map<String, Object> passenger : passengers) {
                    this.simpMessagingTemplate.convertAndSend("/socket-driver-movement/to-passenger/" + passenger.get("id"),
                            socketMessageConverted);
                }
            }
        }

        return socketMessageConverted;
    }


}
