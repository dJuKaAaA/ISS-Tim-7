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

        vehicleService.save(new Vehicle( "Neki tamo model 1", "Redzistrejsn plejt 1", 1, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 2", "Redzistrejsn plejt 2", 2, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 3", "Redzistrejsn plejt 3", 3, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 4", "Redzistrejsn plejt 4", 4, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 5", "Redzistrejsn plejt 5", 5, false, true, null, location));

        // getting dummy data for drivers
//        for (int i = 0; i < 10; ++i) {
//            driverService.save(new Driver());
//        }
        Driver driverWithId1 = driverService.getById(1L);
        documentService.save(new Document(1L, "saobracajna", "", driverWithId1));
        documentService.save(new Document(2L, "vozacka", "", driverWithId1));
        documentService.save(new Document(3L, "licna", "", driverWithId1));
        vehicleService.save(new Vehicle( "Neki tamo model 6", "Redzistrejsn plejt 6", 5, false, true, driverWithId1, location));

        Set<Driver> drivers = new HashSet<>();
        drivers.add(driverWithId1);
        workHoursService.save(new WorkHour(1L, drivers, LocalDateTime.now(), LocalDateTime.now(), Enums.Day.MON));

        Ride ride = new Ride();
        ride.setDriver(driverWithId1);
        rideService.save(ride);
    }

}
