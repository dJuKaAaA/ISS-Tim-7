package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.Month;

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
    private UserActivationService userActivationService;

    @GetMapping
    public void getDummyTestData() {
        Location location = new Location( "Neka tamo lokacija", 1.5, 1.5);
        locationService.save(location);

        vehicleService.save(new Vehicle( "Neki tamo model 1", "Redzistrejsn plejt 1", 1, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 2", "Redzistrejsn plejt 2", 2, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 3", "Redzistrejsn plejt 3", 3, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 4", "Redzistrejsn plejt 4", 4, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 5", "Redzistrejsn plejt 5", 5, false, true, null, location));
        Passenger p = new Passenger();
        passengerService.save(p);
        driverService.saveDriver(new Driver());
        userActivationService.save(new UserActivation(1L, LocalDateTime.now(),LocalDateTime.of(2022, Month.DECEMBER,1,0,0,0),p));

    }

}
