package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.DummyLoginBody;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import com.tim7.iss.tim7iss.services.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/test/dummy/data")
@Transactional
@CrossOrigin
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
    private RidesRepository rideRepository;

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    DriverReviewRepository driverReviewRepository;

    @Autowired
    VehicleReviewRepository vehicleReviewRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private WorkHourService workHoursService;

    @Autowired
    private RideService rideService;

    @PostMapping("/login")
    public ResponseEntity<DummyLoginBody> dummyLogin(@RequestBody DummyLoginBody body) {
        body.setToken("somerandomtokenidontknowwhatimdoingplzhelp");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }   
    @GetMapping("/logout")
    public ResponseEntity<Boolean> dummyLogout() {
        return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public void getDummyTestData() {
//        Location location = new Location("Neka tamo lokacija", 1.5, 1.5);
//        locationService.save(location);
//
//        VehicleType vehicleType = new VehicleType(1L, 100, "Proba");
//        vehicleTypeService.save(vehicleType);
//
//        Driver driverWithId1 = getDriver();
//        driverService.save(driverWithId1);
//        driverWithId1 = driverService.getById(1L);
//
//        vehicleService.save(new Vehicle("Neki tamo model 1", "Redzistrejsn plejt 1", 1, false, true, location));
//        vehicleService.save(new Vehicle("Neki tamo model 2", "Redzistrejsn plejt 2", 2, false, true, location));
//        vehicleService.save(new Vehicle("Neki tamo model 3", "Redzistrejsn plejt 3", 3, false, true, location));
//        vehicleService.save(new Vehicle("Neki tamo model 4", "Redzistrejsn plejt 4", 4, false, true, location));
//        vehicleService.save(new Vehicle("Neki tamo model 5", "Redzistrejsn plejt 5", 5, false, true, location));
//
//        Ride r1 = new Ride();
//        Ride r2 = new Ride();
//        Ride r3 = new Ride();
//        Ride r4 = new Ride();
//        Passenger p = new Passenger();
//        Passenger p1 = new Passenger();
//        Passenger p2 = new Passenger();
//        Passenger p3 = new Passenger();
//
//        p.setFinishedRides(new HashSet<>(Arrays.asList(r1, r2, r3, r4)));
//        p1.setFinishedRides(new HashSet<>(Arrays.asList(r3, r4)));
//        p2.setFinishedRides(new HashSet<>(Arrays.asList(r1, r2, r3)));
//        p3.setFinishedRides(new HashSet<>(Arrays.asList(r2, r3, r4)));
//
////        ridesService.save(r1);
////        ridesService.save(r2);
////        ridesService.save(r3);
////        ridesService.save(r4);
//
//        passengerService.save(p);
//        passengerService.save(p1);
//        passengerService.save(p2);
//        passengerService.save(p3);
//
//        driverService.save(new Driver());
//        userActivationService.save(new UserActivation(1L, LocalDateTime.now(), LocalDateTime.of(2023, Month.DECEMBER, 1, 0, 0, 0), p));

//        Location location = new Location( "Neka tamo lokacija", 1.5, 1.5);
//        locationService.save(location);
//
//        VehicleType vehicleType = new VehicleType(1L, 100, "Proba");
//        vehicleTypeService.save(vehicleType);
//
//        Driver driverWithId1 = getDriver();
//        driverService.save(driverWithId1);
//        driverWithId1 = driverService.getById(1L);
//
//        vehicleService.save(new Vehicle("Neki tamo model 1", "Redzistrejsn plejt 1", 1, false, true, location));
//        vehicleService.save(new Vehicle("Neki tamo model 2", "Redzistrejsn plejt 2", 2, false, true, location));
//        vehicleService.save(new Vehicle("Neki tamo model 3", "Redzistrejsn plejt 3", 3, false, true, location));
//        vehicleService.save(new Vehicle("Neki tamo model 4", "Redzistrejsn plejt 4", 4, false, true, location));
//        vehicleService.save(new Vehicle("Neki tamo model 5", "Redzistrejsn plejt 5", 5, false, true, location));
//
//        Ride r1 = new Ride();
//        Ride r2 = new Ride();
//        Ride r3 = new Ride();
//        Ride r4 = new Ride();
//        Passenger p = new Passenger();
//        Passenger p1 = new Passenger();
//        Passenger p2 = new Passenger();
//        Passenger p3 = new Passenger();
//
//        p.setFinishedRides(new HashSet<>(Arrays.asList(r1, r2, r3, r4)));
//        p1.setFinishedRides(new HashSet<>(Arrays.asList(r3, r4)));
//        p2.setFinishedRides(new HashSet<>(Arrays.asList(r1, r2, r3)));
//        p3.setFinishedRides(new HashSet<>(Arrays.asList(r2, r3, r4)));
//
////        ridesService.save(r1);
////        ridesService.save(r2);
////        ridesService.save(r3);
////        ridesService.save(r4);
//
//        passengerService.save(p);
//        passengerService.save(p1);
//        passengerService.save(p2);
//        passengerService.save(p3);
//
//        driverService.save(new Driver());
//        userActivationService.save(new UserActivation(1L, LocalDateTime.now(), LocalDateTime.of(2022, Month.DECEMBER, 1, 0, 0, 0), p));
//
////        Location location = new Location( "Neka tamo lokacija", 1.5, 1.5);
////        locationService.save(location);
////
////        vehicleService.save(new Vehicle( "Neki tamo model 1", "Redzistrejsn plejt 1", 1, false, true, null, location));
////        vehicleService.save(new Vehicle( "Neki tamo model 2", "Redzistrejsn plejt 2", 2, false, true, null, location));
////        vehicleService.save(new Vehicle( "Neki tamo model 3", "Redzistrejsn plejt 3", 3, false, true, null, location));
////        vehicleService.save(new Vehicle( "Neki tamo model 4", "Redzistrejsn plejt 4", 4, false, true, null, location));
////        vehicleService.save(new Vehicle( "Neki tamo model 5", "Redzistrejsn plejt 5", 5, false, true, null, location));
////        passengerService.save(new Passenger());
//
////        TestReviewController();
////        TestUserControllerGetRides();
////        TestUserControllerGetMessages();
////        TestUserControllerGetNotes();
////
////
//        documentService.save(new Document(1L, driverWithId1,"saobracajna", ""));
//        documentService.save(new Document(2L, driverWithId1,"vozacka", ""));
//        documentService.save(new Document(3L, driverWithId1,"licna", ""));
////
//        workHoursService.save(new WorkHour(1L, driverWithId1, LocalDateTime.now(), LocalDateTime.now()));
////
//        Ride ride = new Ride();
//        ride.setDriver(driverWithId1);
//        rideService.save(ride);
    }

//    private void TestUserControllerGetMessages() {
//
//
//        Driver driver = getDriver();
//        Vehicle vehicle = getVehicle();
//        vehicle.setDriver(driver);
//        driver.setVehicle(vehicle);
//        driverService.save(driver);
//
//        Passenger passenger = getPassenger();
//
//        Ride ride = getRide();
//        ride.setDriver(driver);
//
//        Route route = getRoute();
//        route.setStartingPoint(getLocation());
//        route.setEndPoint(getLocation());
//        route.setRide(ride);
//
//        Set<Route> routes = new HashSet<>();
//        ride.setRoutes(routes);
//
//
//        passenger.setFinishedRides(Set.of(ride));
//        rideRepository.save(ride);
//        passengerService.save(passenger);
//
//        Message message1 = getMessage();
//        message1.setSender(driver);
//        message1.setReceiver(passenger);
//        message1.setContent("message1");
//        message1.setRide(ride);
//
//        Message message2 = getMessage();
//        message2.setSender(passenger);
//        message2.setReceiver(driver);
//        message2.setContent("message2");
//        message2.setRide(ride);
//
//        messageRepository.save(message1);
//        messageRepository.save(message2);
//
//
//    }

//    private void TestUserControllerGetRides() {
//
//
//        Driver driver = getDriver();
//        Vehicle vehicle = getVehicle();
//        vehicle.setDriver(driver);
//        driver.setVehicle(vehicle);
//        driverService.save(driver);
//
//        Passenger passenger = getPassenger();
//
//        Ride ride = getRide();
//        ride.setDriver(driver);
//
//        Route route = getRoute();
//        route.setStartingPoint(getLocation());
//        route.setEndPoint(getLocation());
//        route.setRide(ride);
//
//        Set<Route> routes = new HashSet<>();
//        ride.setRoutes(routes);
//
//        passenger.setFinishedRides(Set.of(ride));
//        rideRepository.save(ride);
//        passengerService.save(passenger);
//
//
//    }

    private void TestUserControllerGetNotes() {
        Passenger passenger = getPassenger();
        Driver driver = getDriver();
        Note note = getNote();

        passengerService.save(passenger);
        driverService.save(driver);

        note.setUser(driver);
        noteRepository.save(note);

    }

//    private void TestReviewController() {
//        Driver driver = getDriver();
//        Vehicle vehicle = getVehicle();
//        vehicle.setDriver(driver);
//        driver.setVehicle(vehicle);
//        driverService.save(driver);
//
//        Passenger passenger = getPassenger();
//
//        Ride ride = getRide();
//        ride.setDriver(driver);
//
//        Route route = getRoute();
//        route.setStartingPoint(getLocation());
//        route.setEndPoint(getLocation());
//        route.setRide(ride);
//
//        Set<Route> routes = new HashSet<>();
//        ride.setRoutes(routes);
//
//        passenger.setFinishedRides(Set.of(ride));
//        rideRepository.save(ride);
//        passengerService.save(passenger);
//
//        VehicleReview review1 = getVehicleReview();
//        review1.setPassenger(passenger);
//        review1.setRide(ride);
//        review1.setVehicle(vehicle);
//        vehicleReviewRepository.save(review1);
//
//
//        Driver driver2 = driverRepository.findById(1L).get();
//        DriverReview review2 = getDriverReview();
//        review2.setPassenger(passenger);
//        review2.setRide(ride);
//        review2.setDriver(driver2);
//        driverRepository.save(driver2);
//        driverReviewRepository.save(review2);
//
//
//    }

    private Document getDocument() {
        Document document = new Document();
        document.setName("Ivan");
        document.setPicturePath("image/img1.png");
        return document;
    }

    private Note getNote() {
        Note note = new Note();
        note.setDate(LocalDateTime.now());
        note.setMessage("Note");
        return note;
    }

    private Location getLocation() {
        Location location = new Location();
        location.setName("Location");
        location.setLatitude(1);
        location.setLatitude(2);
        return location;
    }

    private Route getRoute() {
        Route route = new Route();
        route.setDistance(12);
        return route;
    }

    private Vehicle getVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("BMW");
        vehicle.setRegistrationPlate("123");
        vehicle.setSeatNumber(5);
        vehicle.setBabyAllowed(false);
        vehicle.setPetsAllowed(false);
        vehicle.setDriver(null);
        vehicle.setLocation(null);
        return vehicle;
    }

    private Driver getDriver() {
        Driver driver = new Driver();
        driver.setFirstName("Ivan");
        driver.setLastName("Martic");
        driver.setProfilePicture("");
        driver.setPhoneNumber("0604672999");
        driver.setEmailAddress("ivanmartic311@gmail.com");
        driver.setAddress("7 juli 10");
        driver.setPassword("123");
        driver.setBlocked(false);
        driver.setActive(false);
        Set<Review> driverReviews = new HashSet<>();
        driver.setVehicle(null);
        driver.setWorkHours(null);

        Set<Document> documents = new HashSet<>();
        documents.add(getDocument());
        driver.setDocuments(documents);

        return driver;
    }

    private Passenger getPassenger() {
        Passenger passenger = new Passenger();
        passenger.setFirstName("Vuk");
        passenger.setLastName("Vatovic");
        passenger.setProfilePicture("");
        passenger.setPhoneNumber("0601410299");
        passenger.setAddress("Kosovska 17");
        passenger.setAddress("vukvatovic@gmail.com");
        passenger.setPassword("123");
        passenger.setBlocked(false);
        passenger.setActive(false);
        passenger.setFinishedRides(new HashSet<>());
        passenger.setFavouriteRoutes(new HashSet<>());
        return passenger;
    }

    private Ride getRide() {
        Ride ride = new Ride();
        ride.setPrice(500);
        ride.setEstimatedTimeInMinutes(10);
        ride.setStartTime(LocalDateTime.now());
        ride.setEndTime(LocalDateTime.now());
        ride.setBabyOnBoard(false);
        ride.setPetOnBoard(false);
        ride.setStatus(Enums.RideStatus.ACTIVE);
        ride.setDriver(null);
        ride.setVehicleType(null);
        ride.setRefusal(null);
        ride.setRoutes(null);
        return ride;
    }

    private Message getMessage() {
        Message message = new Message();
        message.setType("STANDARD");
        message.setContent("Message");
        message.setSentDate(LocalDateTime.now());
        return message;
    }

    private VehicleReview getVehicleReview() {
        VehicleReview review = new VehicleReview();
        review.setRating(1);
        review.setComment("Vehicle review");
        return review;
    }

    private DriverReview getDriverReview() {
        DriverReview review = new DriverReview();
        review.setRating(1);
        review.setComment("Driver review");
        return review;
    }

}
