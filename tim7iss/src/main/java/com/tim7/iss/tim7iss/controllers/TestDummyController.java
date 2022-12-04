package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.services.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;

@RestController
@RequestMapping("test/dummy/data")
@Transactional
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

    @Autowired
    private RidesService ridesService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VehicleTypeService vehicleTypeService;
    @GetMapping
    public void getDummyTestData() {
        Location location = new Location( "Neka tamo lokacija", 1.5, 1.5);
        locationService.save(location);

        VehicleType vehicleType = new VehicleType(1L, 100,"Proba", Enums.VehicleName.STANDARD);
        vehicleTypeService.save(vehicleType);

        vehicleService.save(new Vehicle( "Neki tamo model 1", "Redzistrejsn plejt 1", 1, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 2", "Redzistrejsn plejt 2", 2, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 3", "Redzistrejsn plejt 3", 3, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 4", "Redzistrejsn plejt 4", 4, false, true, null, location));
        vehicleService.save(new Vehicle( "Neki tamo model 5", "Redzistrejsn plejt 5", 5, false, true, null, location));

        Ride r1 = new Ride();
        Ride r2 = new Ride();
        Ride r3 = new Ride();
        Ride r4 = new Ride();
        Passenger p = new Passenger();
        Passenger p1 = new Passenger();
        Passenger p2 = new Passenger();
        Passenger p3 = new Passenger();

        p.setFinishedRides(new HashSet<>(Arrays.asList(r1,r2,r3,r4)));
        p1.setFinishedRides(new HashSet<>(Arrays.asList(r3,r4)));
        p2.setFinishedRides(new HashSet<>(Arrays.asList(r1,r2,r3)));
        p3.setFinishedRides(new HashSet<>(Arrays.asList(r2,r3,r4)));

//        ridesService.save(r1);
//        ridesService.save(r2);
//        ridesService.save(r3);
//        ridesService.save(r4);

        passengerService.save(p);
        passengerService.save(p1);
        passengerService.save(p2);
        passengerService.save(p3);

        driverService.saveDriver(new Driver());
        userActivationService.save(new UserActivation(1L, LocalDateTime.now(),LocalDateTime.of(2022, Month.DECEMBER,1,0,0,0),p));
    }

}
