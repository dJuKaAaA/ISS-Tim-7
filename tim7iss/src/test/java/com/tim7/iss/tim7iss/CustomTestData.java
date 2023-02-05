package com.tim7.iss.tim7iss;

import com.tim7.iss.tim7iss.dto.LocationForRideDto;
import com.tim7.iss.tim7iss.dto.RideDto;
import com.tim7.iss.tim7iss.dto.UserDto;
import com.tim7.iss.tim7iss.dto.UserRefDto;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class CustomTestData {

    public String driver2Token = "";
    public String driverToken = "";
    public String passenger1Token = "";
    public String adminToken = "";

    // Test podaci
    public Admin getAdmin() throws IOException {
        Admin admin = new Admin();
        admin.setId(5L);
        admin.setFirstName("Adonis");
        admin.setLastName("Adonis");
        admin.setProfilePicture(DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()));
        admin.setPhoneNumber("003814523423");
        admin.setEmailAddress("admin@email.com");
        admin.setPassword("$2a$12$c9cKc9F6WaOKIchi9bWCpOrWRnXTBEKTU4NFtS3azXhJWy4TAcTey");
        return admin;
    }

    public Passenger getPassenger1() throws IOException {
        Passenger passenger1 = new Passenger(new UserDto(3L, "Petar", "Petrovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372727", "petar.petrovic@email.com", "Petrova adresa", "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123
        passenger1.setId(3L);
        passenger1.setEnabled(true);

        return passenger1;
    }

    public Passenger getPassenger2() throws IOException {
        Passenger passenger2 = new Passenger(new UserDto(4L, "Jovan", "Jovanovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "jovan.jovanovic@email.com", "Jovanova adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passenger2.setEnabled(true);
        passenger2.setId(4L);

        return passenger2;
    }

    public Passenger getPassengerWithNoRides() throws IOException {
        Passenger passenger = new Passenger(new UserDto(null, "Rade", "Radic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "rade" + ".radic@email.com", "Radetova adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passenger.setEnabled(true);
        passenger.setId(6L);
        return passenger;
    }

    public VehicleType getVehicleType() {
        VehicleType vehicleType = new VehicleType(1L, 100, "STANDARD");
        return vehicleType;
    }

    public Driver getDriver() throws IOException {
        VehicleType vehicleType = getVehicleType();

        Driver driver = new Driver(new UserDto(1L, "Mika", "Mikic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "mika.mikic@email.com", "Mikina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
        driver.setEnabled(true);
        driver.setId(1L);


        driver.setVehicle(new Vehicle(1L, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));

        return driver;
    }

    public Driver getDriverWithNoRides() throws IOException {
        VehicleType vehicleType = getVehicleType();
        Driver driverWithNoDrive = new Driver(new UserDto(null, "Pera", "Peric", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "pera.peric@email.com", "Perina adresa", "$2a$12$YzM..B5oG29ezUdF3pC6qexJQpw4UJSxdoaQD9Y.aiURFBuU4/3Qe"));  // Pera1234
        driverWithNoDrive.setEnabled(true);
        driverWithNoDrive.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driverWithNoDrive, new Location(null, "Bistrica, Novi Sad", 45.25207768500065f, 19.799845506488243f)));
        driverWithNoDrive.setEnabled(true);
        driverWithNoDrive.setId(2L);
        return driverWithNoDrive;
    }

    // ID je null
    public Route getRoute() {
        Route route = new Route(null, 1000, 3, new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija", 45.24914205013315f, 19.843100056994654f), new Location(null, "Srpsko narodno pozoriste, Pozorisni trg, Novi Sad, Srbija", 45.25510777309239f, 19.842949154190308f));
        return route;
    }

    public Ride getPendingRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger1 = getPassenger1();
        Passenger passenger2 = getPassenger2();
        Route route = getRoute();
        route.setId(1L);

        Ride pendingRide = new Ride(1L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1, passenger2), null, List.of(route));

        return pendingRide;
    }

    public Ride getActiveRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(2L);

        Ride activeRide = new Ride(2L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACTIVE, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        return activeRide;
    }

    public Ride getFinishedRide() throws IOException {

        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(3L);

        Ride finishedRide = new Ride(3L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), LocalDateTime.of(2023, Month.JANUARY, 19, 16, 20), route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.FINISHED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        return finishedRide;
    }

    public Ride getRejectedRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(4L);

        Ride rejectedRide = new Ride(4L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.REJECTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        Refusal refusal = new Refusal(null, driver, "Refusal", LocalDateTime.now().minusMinutes(195), rejectedRide);
        rejectedRide.setRefusal(refusal);
        return rejectedRide;
    }

    public Ride getAcceptedRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(5L);

        Ride acceptedRide = new Ride(5L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        return acceptedRide;
    }

    public FavoriteLocation getFavoriteLocation() throws IOException {
        VehicleType vehicleType = new VehicleType(1L, 100, "STANDARD");
        FavoriteLocation favoriteLocation = new FavoriteLocation();
        favoriteLocation.setId(1L);
        favoriteLocation.setFavoriteName("Home to Work");
        favoriteLocation.setRoutes(Set.of(getRoute()));
        favoriteLocation.setPassengers(Set.of(getPassenger1()));
        favoriteLocation.setVehicleType(vehicleType);
        favoriteLocation.setBabyTransport(false);
        favoriteLocation.setPetTransport(false);
        return favoriteLocation;
    }

    public RideDto prepareRideDtoBeforeAssertion(RideDto rideDto) {
        List<UserRefDto> passengers = rideDto.getPassengers();
        passengers.sort(Comparator.comparing(UserRefDto::getId));
        rideDto.setPassengers(passengers);

        List<LocationForRideDto> locations = rideDto.getLocations();
        locations.sort(Comparator.comparing(LocationForRideDto::getDistanceInMeters));
        rideDto.setLocations(locations);

        return rideDto;
    }

    public Ride getRideForDriverAtThatMomentTest(Enums.RideStatus status, LocalDateTime moment, Driver driver) throws IOException {
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(8L);
        Ride ride = new Ride(7L, 1000, moment, null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, status, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));
        return ride;
    }

    public Ride getPendingRideWithPassenger(Passenger passenger) throws IOException {
        Route route = getRoute();
        Driver driver = getDriver();
        Ride ride = new Ride(8L, 1000, LocalDateTime.now().plusMinutes(15), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));
        return ride;
    }

    public Driver getDriver2() throws IOException {
        VehicleType vehicleType = getVehicleType();

        Driver driver2 = new Driver(new UserDto(null, "Pera", "Peric", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "pera@email.com", "Perina adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));
        driver2.setEnabled(true);
        driver2.setId(7L);

        driver2.setVehicle(new Vehicle(5L, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver2, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));

        return driver2;
    }

    public Ride getRideForWithdrawal() throws IOException {
        Driver driver = getDriver();
        Passenger passenger1 = getPassenger1();
        Route route = getRoute();
        route.setId(6L);

        Ride pendingRide = new Ride(6L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route));

        return pendingRide;
    }
    public Ride getRideForCancelation() throws IOException {
        Driver driver = getDriver();
        Passenger passenger1 = getPassenger1();
        Route route = getRoute();
        route.setId(1L);

        Ride pendingRide = new Ride(7L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route));

        return pendingRide;
    }
    public Ride getRideForAcceptance() throws IOException {
        Driver driver = getDriver();
        Passenger passenger1 = getPassenger1();
        Route route = getRoute();
        route.setId(1L);

        Ride pendingRide = new Ride(8L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route));

        return pendingRide;
    }
    public Ride getDriver2ActiveRide() throws IOException {
        Driver driver = getDriver2();
        Passenger passenger2 = getPassenger2();
        Route route = getRoute();
        route.setId(1L);

        Ride pendingRide = new Ride(9L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACTIVE, driver, driver.getVehicle().getVehicleType(), Set.of(passenger2), null, List.of(route));

        return pendingRide;
    }

}
