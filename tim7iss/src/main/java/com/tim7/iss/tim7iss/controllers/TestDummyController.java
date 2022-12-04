package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("test/dummy/data")
public class TestDummyController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private WorkHourService workHoursService;

    @Autowired
    private RideService rideService;

    @PostMapping
    public void getDummyTestData() {
        Location location = new Location( "Neka tamo lokacija", 1.5, 1.5);
        locationService.save(location);

        Driver driverWithId1 = driverService.getById(1L);
        documentService.save(new Document(1L, "saobracajna", "", driverWithId1));
        documentService.save(new Document(2L, "vozacka", "", driverWithId1));
        documentService.save(new Document(3L, "licna", "", driverWithId1));

        Set<Driver> drivers = new HashSet<>();
        drivers.add(driverWithId1);
        workHoursService.save(new WorkHour(1L, drivers, LocalDateTime.now(), LocalDateTime.now(), Enums.Day.MON));

        Ride ride = new Ride();
        ride.setDriver(driverWithId1);
        rideService.save(ride);
    }

}
