package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.LocationService;
import com.tim7.iss.tim7iss.services.PassengerService;
import com.tim7.iss.tim7iss.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;
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
    RideRepository rideRepository;

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
    @GetMapping
    public void getDummyTestData() {
//        Location location = new Location( "Neka tamo lokacija", 1.5, 1.5);
//        locationService.save(location);
//
//        vehicleService.save(new Vehicle( "Neki tamo model 1", "Redzistrejsn plejt 1", 1, false, true, null, location));
//        vehicleService.save(new Vehicle( "Neki tamo model 2", "Redzistrejsn plejt 2", 2, false, true, null, location));
//        vehicleService.save(new Vehicle( "Neki tamo model 3", "Redzistrejsn plejt 3", 3, false, true, null, location));
//        vehicleService.save(new Vehicle( "Neki tamo model 4", "Redzistrejsn plejt 4", 4, false, true, null, location));
//        vehicleService.save(new Vehicle( "Neki tamo model 5", "Redzistrejsn plejt 5", 5, false, true, null, location));
//        passengerService.save(new Passenger());

        TestReviewController();
//        TestUserControllerGetRides();
//        TestUserControllerGetMessages();
//        TestUserControllerGetNotes();


    }

    private void TestUserControllerGetMessages() {


        Driver driver = getDriver();
        Vehicle vehicle = getVehicle();
        vehicle.setDriver(driver);
        driver.setVehicle(vehicle);
        driverService.saveDriver(driver);

        Passenger passenger = getPassenger();

        Ride ride = getRide();
        ride.setDriver(driver);

        Route route = getRoute();
        route.setStartingPoint(getLocation());
        HashSet<Location> locations = new HashSet<>();
        locations.add(getLocation());
        route.setEndPoints(locations);
        route.setRide(ride);
        ride.setRoute(route);


        passenger.setFinishedRides(Set.of(ride));
        rideRepository.save(ride);
        passengerService.save(passenger);

        Message message1 = getMessage();
        message1.setSender(driver);
        message1.setReceiver(passenger);
        message1.setContent("message1");
        message1.setRide(ride);

        Message message2 = getMessage();
        message2.setSender(passenger);
        message2.setReceiver(driver);
        message2.setContent("message2");
        message2.setRide(ride);

        messageRepository.save(message1);
        messageRepository.save(message2);


    }

    private void TestUserControllerGetRides() {


        Driver driver = getDriver();
        Vehicle vehicle = getVehicle();
        vehicle.setDriver(driver);
        driver.setVehicle(vehicle);
        driverService.saveDriver(driver);

        Passenger passenger = getPassenger();

        Ride ride = getRide();
        ride.setDriver(driver);

        Route route = getRoute();
        route.setStartingPoint(getLocation());
        HashSet<Location> locations = new HashSet<>();
        locations.add(getLocation());
        route.setEndPoints(locations);
        route.setRide(ride);
        ride.setRoute(route);


        passenger.setFinishedRides(Set.of(ride));
        rideRepository.save(ride);
        passengerService.save(passenger);



    }

    private void TestUserControllerGetNotes() {
        Passenger passenger = getPassenger();
        Driver driver = getDriver();
        Note note = getNote();

        passengerService.save(passenger);
        driverService.saveDriver(driver);

        note.setUser(driver);
        noteRepository.save(note);

    }

    private void TestReviewController(){
        Driver driver = getDriver();
        Vehicle vehicle = getVehicle();
        vehicle.setDriver(driver);
        driver.setVehicle(vehicle);
        driverService.saveDriver(driver);

        Passenger passenger = getPassenger();

        Ride ride = getRide();
        ride.setDriver(driver);

        Route route = getRoute();
        route.setStartingPoint(getLocation());
        HashSet<Location> locations = new HashSet<>();
        locations.add(getLocation());
        route.setEndPoints(locations);
        route.setRide(ride);
        ride.setRoute(route);


        passenger.setFinishedRides(Set.of(ride));
        rideRepository.save(ride);
        passengerService.save(passenger);

        VehicleReview review1 = getVehicleReview();
        review1.setPassenger(passenger);
        review1.setRide(ride);
        review1.setVehicle(vehicle);
        vehicleReviewRepository.save(review1);


        Driver driver2 = driverRepository.findById(1L).get();
        DriverReview review2 = getDriverReview();
        review2.setPassenger(passenger);
        review2.setRide(ride);
        review2.setDriver(driver2);
        driverRepository.save(driver2);
        driverReviewRepository.save(review2);


    }

    private Document getDocument(){
        Document document = new Document();
        document.setName("Ivan");
        document.setPicturePath("image/img1.png");
        return document;
    }
    private Note getNote(){
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
        driver.setSentMessages(new HashSet<>());
        driver.setReceivedMessages(new HashSet<>());
        driver.setRefusals(new HashSet<>());
        Set<Review> driverReviews = new HashSet<>();
        driver.setReviews(driverReviews);
        driver.setVehicle(null);
        driver.setWorkHours(null);
        driver.setRides(new HashSet<>());

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
        passenger.setSentMessages(new HashSet<>());
        passenger.setReceivedMessages(new HashSet<>());
        passenger.setRefusals(new HashSet<>());
        passenger.setReviews(new HashSet<>());
        passenger.setFinishedRides(new HashSet<>());
        passenger.setFavouriteRoutes(new HashSet<>());
        return passenger;
    }

    private Ride getRide() {
        Ride ride = new Ride();
        ride.setPrice(500);
        ride.setEstimatedTimeInMinutes(10);
        ride.setStartDate(LocalDateTime.now());
        ride.setEndDate(LocalDateTime.now());
        ride.setBabyOnBoard(false);
        ride.setPetOnBoard(false);
        ride.setStatus(Enums.RideStatus.ACTIVE);
        ride.setDriver(null);
        ride.setVehicleType(null);
        ride.setMessages(new HashSet<>());
        ride.setPassengers(new HashSet<>());
        ride.setRefusal(null);
        ride.setReviews(new HashSet<>());
        ride.setRoute(null);
        return ride;
    }

    private Message getMessage() {
        Message message = new Message();
        message.setType(Enums.MessageType.RIDE);
        message.setContent("Message");
        message.setSentDate(LocalDateTime.now());
        return message;
    }

    private VehicleReview getVehicleReview(){
        VehicleReview review = new VehicleReview();
        review.setRating(1);
        review.setComment("Vehicle review");
        return review;
    }
    private DriverReview getDriverReview(){
        DriverReview review = new DriverReview();
        review.setRating(1);
        review.setComment("Driver review");
        return review;
    }


}
