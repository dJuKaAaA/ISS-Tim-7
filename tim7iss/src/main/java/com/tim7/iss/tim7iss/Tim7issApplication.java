package com.tim7.iss.tim7iss;

import com.tim7.iss.tim7iss.dto.UserDto;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

@SpringBootApplication
public class Tim7issApplication {

    @Autowired
    DriverDocumentRequestRepository driverDocumentRequestRepository;
    @Autowired
    DriverRequestRepository driverRequestRepository;
    @Autowired
    RefusalRepository refusalRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private VehicleReviewRepository vehicleReviewRepository;
    @Autowired
    private PanicRepository panicRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DriverReviewRepository driverReviewRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private UserActivationRepository userActivationRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private FavoriteLocationRepository favoriteLocationRepository;


    public static void main(String[] args) {
        SpringApplication.run(Tim7issApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // DISCLAIMER: for testing purposes
    @Bean
    protected InitializingBean sendDatabase() {
//        return this::testDataDjukanovic;
//        return this::testDataMartic;
//        return this::testDataStanojlovic;
        return this::generateTestDataInDataBase;
//        return this::projectDefenceTestData;
    }

    private void testDataDjukanovic() throws IOException {
        Random random = new Random();

        // vehicle type creation
        VehicleType vehicleType = vehicleTypeRepository.save(new VehicleType(null, 100, "STANDARD"));

        // role creation
        Role adminRole = roleRepository.save(new Role(null, "ROLE_ADMIN"));
        Role driverRole = roleRepository.save(new Role(null, "ROLE_DRIVER"));
        Role passengerRole = roleRepository.save(new Role(null, "ROLE_PASSENGER"));
        Role anonymousRole = roleRepository.save(new Role(null, "ROLE_ANONYMOUS"));

        // Admin creation
        Admin admin = new Admin();
        admin.setFirstName("Adonis");
        admin.setLastName("Adonis");
        admin.setProfilePicture(DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()));
        admin.setPhoneNumber("003814523423");
        admin.setEmailAddress("admin@email.com");
        admin.setPassword("$2a$12$c9cKc9F6WaOKIchi9bWCpOrWRnXTBEKTU4NFtS3azXhJWy4TAcTey");  // Admin123
        admin.setEnabled(true);
        admin.setRoles(List.of(adminRole));
        adminRepository.save(admin);

        // driver creation
        Driver driver = new Driver(new UserDto(null, "Mika", "Mikic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "mika.mikic@email.com", "Mikina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
        driver.setEnabled(true);
        driver.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));
        driver.setWorkHours(Set.of(new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
        driver.setRoles(List.of(driverRole));
        driverRepository.save(driver);

        // passenger creation
        Passenger passenger1 = new Passenger(new UserDto(null, "Petar", "Petrovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372727", "petar.petrovic@email.com", "Petrova adresa", "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123
        passenger1.setRoles(List.of(passengerRole));
        passenger1.setEnabled(true);
        passengerRepository.save(passenger1);
        Passenger passenger2 = new Passenger(new UserDto(null, "Jovan", "Jovanovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "jovan.jovanovic@email.com", "Jovanova adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passenger2.setRoles(List.of(passengerRole));
        passenger2.setEnabled(true);
        passengerRepository.save(passenger2);
        Passenger passenger3 = new Passenger(new UserDto(null, "Mirko", "Mirkovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "00381737111", "mirko.mirkovic@email.com", "Mirkova adresa", "$2a$12$nYULTJpydL5pFRSxQ30DnOlhu/m/O6U4CoWLqea82PYKNsswHCEsG"));  // Mirko123
        passenger3.setRoles(List.of(passengerRole));
        passenger3.setEnabled(true);
        passengerRepository.save(passenger3);
        Passenger passenger4 = new Passenger(new UserDto(null, "Aleksandar", "Popovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "0038173724234", "aleksandar.popovic@email.com", "Aleksandrova adresa", "$2a$12$yNYY1KoO4DDFSLzqRBXPJ.EJU3us/O8ws5d45pQ856BoiS70mslyu"));  // Aleksandar123
        passenger4.setRoles(List.of(passengerRole));
        passenger4.setEnabled(true);
        passengerRepository.save(passenger4);
        Passenger passenger5 = new Passenger(new UserDto(null, "Vuk", "Perisic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372727", "vuk.perisic@email.com", "Vukova adresa", "$2a$12$fCHBcs11T5oI78NcRModiuCn5TjFE1QLn9x1awvf0meSO7LqSepc2"));  // Vuk12345
        passenger5.setRoles(List.of(passengerRole));
        passenger5.setEnabled(true);
        passengerRepository.save(passenger5);


        // declaring routes that will be saved when the ride that contains them gets created
        Route route1 = new Route(null, 1000, 3, new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija", 45.24914205013315f, 19.843100056994654f), new Location(null, "Srpsko narodno pozorište, Pozorišni trg, Novi Sad, Srbija", 45.25510777309239f, 19.842949154190308f));
        Route route2 = new Route(null, 1900, 5, new Location(null, "Katolicka Porta 4, Novi Sad", 45.25642044682303f, 19.845453240699275f), new Location(null, "Dunavski Park, Dunavska, Novi Sad, Srbija", 45.25539880319645f, 19.85058802720829f));
        Route route3 = new Route(null, 3600, 9, new Location(null, "NTP NS, Fruškogorska, Novi Sad, Serbia", 45.24475880747688f, 19.84708251359552f), new Location(null, "Knin, Novi Sad, Serbia", 45.25433739645546f, 19.820878211862986f));
        Route route4 = new Route(null, 6700, 10, new Location(null, "Prime Training Facility, Svetozara Miletića, Novi Sad, Srbija", 45.257847680306085f, 19.843377640699245f), new Location(null, "Police Academy, Železnička, Sremska Kamenica, Srbija", 45.22163647056052f, 19.850156546431652f));
        Route route5 = new Route(null, 5400, 11, new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija", 45.24914205013315f, 19.843100056994654f), new Location(null, "Sportski Centar “Slana Bara”, Sentandrejski put, Novi Sad, Srbija", 45.29135042557076f, 19.824244340624777f));

        // ride creation
        // TODO: Make at least 20
        // TODO: Place the right price depending on the vehicle type and route distance
        // TODO: Place the right ride length
        Ride ride1 = rideRepository.save(new Ride(null, 1000, LocalDateTime.now().plusMinutes(random.nextInt(60, 1400)), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1, passenger2, passenger3), null, List.of(route1.clone())));
        Ride ride2 = rideRepository.save(new Ride(null, 1000, LocalDateTime.now().plusMinutes(random.nextInt(1500, 2800)), LocalDateTime.now().plusMinutes(random.nextInt(2900, 3000)), route3.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.FINISHED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger4, passenger5), null, List.of(route3.clone())));
        Ride ride3 = rideRepository.save(new Ride(null, 1000, LocalDateTime.now().plusMinutes(30), null, route4.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger2, passenger3, passenger5), null, List.of(route4.clone())));
        Ride ride4 = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2022, Month.OCTOBER, 1, 11, 23), LocalDateTime.of(2022, Month.OCTOBER, 1, 12, 1), route2.getEstimatedTimeInMinutes(), driver.getVehicle().isBabyAllowed(), false, false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1, passenger2, passenger4), null, List.of(route2.clone())));
        Ride ride5 = rideRepository.save(new Ride(null, 1000, LocalDateTime.now().plusMinutes(random.nextInt(3100, 4200)), null, route5.getEstimatedTimeInMinutes(), false, false, true, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1, passenger3, passenger5), null, List.of(route5.clone())));
        Ride ride6 = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2022, Month.NOVEMBER, 1, 9, 3), LocalDateTime.of(2022, Month.NOVEMBER, 1, 9, 25), route5.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.FINISHED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger5), null, List.of(route5.clone())));
        Ride ride7 = rideRepository.save(new Ride(null, 1000, LocalDateTime.now().plusMinutes(random.nextInt(4300, 6000)), null, route3.getEstimatedTimeInMinutes(), driver.getVehicle().isBabyAllowed(), driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger4), null, List.of(route3.clone())));

        // creating vehicles
        locationRepository.save(new Location(null, "Valentina Vodnika 10, Novi Sad", 45.255956f, 19.8366902f));
        locationRepository.save(new Location(null, "Beogradska 7, Petrovaradin", 45.254896f, 19.8612956f));
        vehicleRepository.save(new Vehicle(null, "BMW iXM60", "PGAA112", 5, false, false, vehicleType, null, new Location(null, "Valentina Vodnika 10, Novi Sad", 45.255956f, 19.8366902f)));
        vehicleRepository.save(new Vehicle(null, "BMW iX3", "PGAA113", 5, true, true, vehicleType, null, new Location(null, "Beogradska 7, Petrovaradin", 45.254896f, 19.8612956f)));

        // panic creation
        // TODO: Enter valid date time according to ride start date
        panicRepository.save(new Panic(null, LocalDateTime.now(), false, "Driver is very uncomfortable and is making racist comments", ride7, passenger4));

        // review creation
        DriverReview driverReview = new DriverReview();
        driverReview.setRating(5f);
        driverReview.setComment("This driver is awesome!");
        driverReview.setRide(ride4);
        driverReview.setPassenger(passenger1);
        driverReview.setDriver(ride4.getDriver());
        VehicleReview vehicleReview = new VehicleReview();
        vehicleReview.setRating(5f);
        vehicleReview.setComment("This car is very clean!");
        vehicleReview.setRide(ride4);
        vehicleReview.setPassenger(passenger1);
        vehicleReview.setVehicle(ride4.getDriver().getVehicle());
        driverReviewRepository.save(driverReview);
        vehicleReviewRepository.save(vehicleReview);
        messageRepository.save(new Message(null, LocalDateTime.now(), "Ride", "proba", driver, passenger2, ride4));
        messageRepository.save(new Message(null, LocalDateTime.now(), "Ride", "proba", passenger3, driver, ride4));

        // aditional driver
        Driver driver2 = new Driver(new UserDto(null, "Pera", "Peric", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "pera.peric@email.com", "Perina adresa", "$2a$12$YzM..B5oG29ezUdF3pC6qexJQpw4UJSxdoaQD9Y.aiURFBuU4/3Qe"));  // Pera1234
        driver2.setEnabled(true);
        driver2.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver2, new Location(null, "Bistrica, Novi Sad", 45.25207768500065f, 19.799845506488243f)));
        driver2.setRoles(List.of(driverRole));
        driverRepository.save(driver2);
    }

    private void testDataMartic() throws IOException {

    }

    private void testDataStanojlovic() {

    }

    private void generateTestDataInDataBase() throws IOException {


        Random random = new Random();

        // vehicle type creation

        // vehicle type creation
        // id = 1
        VehicleType vehicleType = vehicleTypeRepository.save(new VehicleType(null, 100, "STANDARD"));

        // role creation
        Role adminRole = roleRepository.save(new Role(null, "ROLE_ADMIN"));
        Role driverRole = roleRepository.save(new Role(null, "ROLE_DRIVER"));
        Role passengerRole = roleRepository.save(new Role(null, "ROLE_PASSENGER"));
        Role anonymousRole = roleRepository.save(new Role(null, "ROLE_ANONYMOUS"));

        // id = 1
        Driver driver = new Driver(new UserDto(null, "Mika", "Mikic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "mika.mikic@email.com", "Mikina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
        driver.setEnabled(true);

        // vehicleId = 1
        driver.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));

        driver.setWorkHours(Set.of(new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
        driver.setRoles(List.of(driverRole));
        driverRepository.save(driver);

        // id = 2
        Driver driverWithNoDrive = new Driver(new UserDto(null, "Pera", "Peric", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "pera.peric@email.com", "Perina adresa", "$2a$12$YzM..B5oG29ezUdF3pC6qexJQpw4UJSxdoaQD9Y.aiURFBuU4/3Qe"));  // Pera1234
        driverWithNoDrive.setEnabled(true);
        driverWithNoDrive.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driverWithNoDrive, new Location(null, "Bistrica, Novi Sad", 45.25207768500065f, 19.799845506488243f)));
        driverWithNoDrive.setRoles(List.of(driverRole));
        driverWithNoDrive.setEnabled(true);
        driverRepository.save(driverWithNoDrive);


        // passenger creation
        // id = 3
        Passenger passenger1 = new Passenger(new UserDto(null, "Petar", "Petrovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372727", "petar.petrovic@email.com", "Petrova adresa", "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123
        passenger1.setRoles(List.of(passengerRole));
        passenger1.setEnabled(true);
        passengerRepository.save(passenger1);

        // id = 4
        Passenger passenger2 = new Passenger(new UserDto(null, "Jovan", "Jovanovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "jovan.jovanovic@email.com", "Jovanova adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passenger2.setRoles(List.of(passengerRole));
        passenger2.setEnabled(true);
        passengerRepository.save(passenger2);


        // id = 5
        Admin admin = new Admin();
        admin.setFirstName("Adonis");
        admin.setLastName("Adonis");
        admin.setProfilePicture(DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()));
        admin.setPhoneNumber("003814523423");
        admin.setEmailAddress("admin@email.com");
        admin.setPassword("$2a$12$c9cKc9F6WaOKIchi9bWCpOrWRnXTBEKTU4NFtS3azXhJWy4TAcTey");  // Admin123
        admin.setEnabled(true);
        admin.setRoles(List.of(adminRole));
        adminRepository.save(admin);


        // id = 6
        Passenger passenger3 = new Passenger(new UserDto(null, "Rade", "Radic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "rade" + ".radic@email.com", "Radetova adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passenger3.setRoles(List.of(passengerRole));
        passenger3.setEnabled(true);
        passengerRepository.save(passenger3);

        // id = 7
        Passenger passengerWhoIsNotSupposedToHaveAnyAcceptedRides = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "neko.nekic@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerWhoIsNotSupposedToHaveAnyAcceptedRides.setRoles(List.of(passengerRole));
        passengerWhoIsNotSupposedToHaveAnyAcceptedRides.setEnabled(true);
        passengerRepository.save(passengerWhoIsNotSupposedToHaveAnyAcceptedRides);


        // id = 8
        Driver hopefullyAvailableDriver = new Driver(new UserDto(null, "Pera", "Peric", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "pera.peric@email.com", "Perina adresa", "$2a$12$YzM..B5oG29ezUdF3pC6qexJQpw4UJSxdoaQD9Y.aiURFBuU4/3Qe"));  // Pera1234
        hopefullyAvailableDriver.setEnabled(true);
        hopefullyAvailableDriver.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, hopefullyAvailableDriver, new Location(null, "Bistrica, Novi Sad", 45.25207768500065f, 19.799845506488243f)));
        hopefullyAvailableDriver.setRoles(List.of(driverRole));
        hopefullyAvailableDriver.setEnabled(true);
        hopefullyAvailableDriver.setActive(true);
        driverRepository.save(hopefullyAvailableDriver);

        // id = 9
        Passenger anotherPassengerWhoIsNotSupposedToHaveAnyAcceptedRides = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "neko2.nekic@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        anotherPassengerWhoIsNotSupposedToHaveAnyAcceptedRides.setRoles(List.of(passengerRole));
        anotherPassengerWhoIsNotSupposedToHaveAnyAcceptedRides.setEnabled(true);
        passengerRepository.save(anotherPassengerWhoIsNotSupposedToHaveAnyAcceptedRides);

        // declaring routes that will be saved when the ride that contains them gets created
        // id = 1
        Route route1 = new Route(null, 1000, 10, new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija", 45.24914205013315f, 19.843100056994654f), new Location(null, "Srpsko narodno pozoriste, Pozorisni trg, Novi Sad, Srbija", 45.25510777309239f, 19.842949154190308f));

        // ride creation
        // PENDING id = 1
        Ride pendingRide = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1, passenger2), null, List.of(route1.clone())));


        // ACTIVE id = 2
        Ride activeRide = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACTIVE, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route1.clone())));

        // FINISHED id = 3
        Ride finishedRide = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), LocalDateTime.of(2023, Month.JANUARY, 19, 16, 20), route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.FINISHED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route1.clone())));

        // REJECTED id = 4
        Ride rejectedRide = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.REJECTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route1.clone())));
        Refusal refusal = new Refusal(null, driver, "Refusal", LocalDateTime.now().minusMinutes(195), rejectedRide);
        refusalRepository.save(refusal);

        // ACCEPTED RIDE id = 5
        Ride acceptedRide = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route1.clone())));

        // Ride that is supposed to be deleted during the tests -> id = 6
        Ride rideToBeDeleted = rideRepository.save(new Ride(null, 1000, LocalDateTime.now().plusMinutes(30), null, route1.getEstimatedTimeInMinutes(), false, false, false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger2), null, List.of(route1.clone())));

        // id = 7
        Ride rideForDriverAtThatMomentTest = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.MAY, 19, 8, 1), null, route1.getEstimatedTimeInMinutes(), false, false, false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route1.clone())));

        // Ride for withdrawal id = 6
        Ride rideForWithdrawal = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route1.clone())));

        //Ride for cancelation id = 7
        Ride rideForCancelation = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route1.clone())));

        //Ride for acceptance id = 8
        Ride rideForAcceptance = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1), null, List.of(route1.clone())));

        // id = 7
        Driver driver2 = new Driver(new UserDto(null, "Pera", "Peric", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "pera@email.com", "Perina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));
        driver2.setRoles(List.of(driverRole));
        driver2.setEnabled(true);
        driver2.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver2, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));
        driver2.setWorkHours(Set.of(new WorkHour(null, driver2, LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)), new WorkHour(null, driver2, LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)), new WorkHour(null, driver2, LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)), new WorkHour(null, driver2, LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
        driverRepository.save(driver2);

        //Driver2 acive ride id = 9
        Ride driver2ActiveRide = rideRepository.save(new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route1.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACTIVE, driver2, driver2.getVehicle().getVehicleType(), Set.of(passenger2), null, List.of(route1.clone())));

        // Favorite location
        // id = 1
        FavoriteLocation favoriteLocation = new FavoriteLocation();
        favoriteLocation.setFavoriteName("Home to Work");
        favoriteLocation.setRoutes(Set.of(route1));
        favoriteLocation.setPassengers(Set.of(passenger1));
        favoriteLocation.setVehicleType(vehicleType);
        favoriteLocation.setBabyTransport(false);
        favoriteLocation.setPetTransport(false);
        favoriteLocationRepository.save(favoriteLocation);

        // creating vehicles
        vehicleRepository.save(new Vehicle(null, "BMW iXM60", "PGAA112", 5, false, false, vehicleType, null, new Location(null, "Valentina Vodnika 10, Novi Sad", 45.255956f, 19.8366902f)));
        vehicleRepository.save(new Vehicle(null, "BMW iX3", "PGAA113", 5, true, true, vehicleType, null, new Location(null, "Beogradska 7, Petrovaradin", 45.254896f, 19.8612956f)));

        Document document = new Document();
        document.setPicture(DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()));
        document.setName("Licna karta");
        document.setDriver(driver);
        documentRepository.save(document);

        // schedule ride selenium tests
        // DO NOT TOUCH !!!
        Passenger passengerSeleniumTestImmediateSchedule = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "immediate.schedule.success@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerSeleniumTestImmediateSchedule.setRoles(List.of(passengerRole));
        passengerSeleniumTestImmediateSchedule.setEnabled(true);
        passengerRepository.save(passengerSeleniumTestImmediateSchedule);
        Passenger passengerSeleniumTestClearingRoutesSchedule = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "clearing.routes.schedule@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerSeleniumTestClearingRoutesSchedule.setRoles(List.of(passengerRole));
        passengerSeleniumTestClearingRoutesSchedule.setEnabled(true);
        passengerRepository.save(passengerSeleniumTestClearingRoutesSchedule);
        Passenger passengerSeleniumTestPastTimeScheduling = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "past.time.schedule@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerSeleniumTestPastTimeScheduling.setRoles(List.of(passengerRole));
        passengerSeleniumTestPastTimeScheduling.setEnabled(true);
        passengerRepository.save(passengerSeleniumTestPastTimeScheduling);
        Passenger passengerSeleniumTestPassengerInvitedSchedule = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "passenger.inviting.schedule@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerSeleniumTestPassengerInvitedSchedule.setRoles(List.of(passengerRole));
        passengerSeleniumTestPassengerInvitedSchedule.setEnabled(true);
        passengerRepository.save(passengerSeleniumTestPassengerInvitedSchedule);
        Passenger passengerSeleniumTestGuestPassenger = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "guest.passenger.schedule@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerSeleniumTestGuestPassenger.setRoles(List.of(passengerRole));
        passengerSeleniumTestGuestPassenger.setEnabled(true);
        passengerRepository.save(passengerSeleniumTestGuestPassenger);
        Passenger passengerSeleniumTestLaterTimeScheduling = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "later.time.schedule@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerSeleniumTestLaterTimeScheduling.setRoles(List.of(passengerRole));
        passengerSeleniumTestLaterTimeScheduling.setEnabled(true);
        passengerRepository.save(passengerSeleniumTestLaterTimeScheduling);
        Passenger passengerSeleniumTestInviteThenRemoveGuest = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "remove.guest.schedule@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerSeleniumTestInviteThenRemoveGuest.setRoles(List.of(passengerRole));
        passengerSeleniumTestInviteThenRemoveGuest.setEnabled(true);
        passengerRepository.save(passengerSeleniumTestInviteThenRemoveGuest);
        Passenger passengerSeleniumTestSetFavoriteScheduling = new Passenger(new UserDto(null, "Neko", "Nekic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "set.favorite.schedule@email.com", "Neka adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passengerSeleniumTestSetFavoriteScheduling.setRoles(List.of(passengerRole));
        passengerSeleniumTestSetFavoriteScheduling.setEnabled(true);
        passengerRepository.save(passengerSeleniumTestSetFavoriteScheduling);
    }

    private void projectDefenceTestData() throws IOException {

        Random random = new Random();

        // vehicle type creation
        VehicleType standardVehicleType = vehicleTypeRepository.save(new VehicleType(null, 100, "STANDARD"));
        VehicleType vanVehicleType = vehicleTypeRepository.save(new VehicleType(null, 50, "VAN"));
        VehicleType luxuryVehicleType = vehicleTypeRepository.save(new VehicleType(null, 300, "LUXURY"));

        // role creation
        Role adminRole = roleRepository.save(new Role(null, "ROLE_ADMIN"));
        Role driverRole = roleRepository.save(new Role(null, "ROLE_DRIVER"));
        Role passengerRole = roleRepository.save(new Role(null, "ROLE_PASSENGER"));
        Role anonymousRole = roleRepository.save(new Role(null, "ROLE_ANONYMOUS"));

        // Admin creation
        Admin admin = new Admin();
        admin.setFirstName("Andrew");
        admin.setLastName("Tate");
        admin.setProfilePicture(DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()));
        admin.setPhoneNumber("+3814523423");
        admin.setEmailAddress("andrewtate@email.com");
        admin.setPassword("$2a$12$c9cKc9F6WaOKIchi9bWCpOrWRnXTBEKTU4NFtS3azXhJWy4TAcTey");  // Admin123
        admin.setEnabled(true);
        admin.setRoles(List.of(adminRole));
        adminRepository.save(admin);

        // driver creation
        //
        //
        Driver driver = new Driver(new UserDto(null, "Mika", "Mikic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "mika.mikic@email.com", "Mikina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
        driver.setEnabled(true);
        driver.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, standardVehicleType, driver, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));
        driver.setWorkHours(Set.of(new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
        driver.setRoles(List.of(driverRole));
        driverRepository.save(driver);

        Driver sasaMatic = new Driver(new UserDto(null, "Sasa", "Matic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/sasamaticprofilepicture.jpg")), "+3817278820", "sasa.matic@email.com", "Poklonite mi nju za rodjendan", "$2a$12$Y.3lrc0GizKJMHNnREtNPOqgJNglwezV.dwPVHn0NJ1xuxe4TlPbu"));  // Nekadaisad1
        sasaMatic.setEnabled(true);
        sasaMatic.setVehicle(new Vehicle(null, "BMW X2", "PGAA121", 5, true, true, luxuryVehicleType, sasaMatic, new Location(null, "Dunavski Park, Dunavska, Novi Sad, Srbija", 45.25539880319645f, 19.85058802720829f)));
        sasaMatic.setWorkHours(Set.of(new WorkHour(null, sasaMatic, LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)), new WorkHour(null, sasaMatic, LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)), new WorkHour(null, sasaMatic, LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)), new WorkHour(null, sasaMatic, LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)), new WorkHour(null, sasaMatic, LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
        sasaMatic.setRoles(List.of(driverRole));
        driverRepository.save(sasaMatic);

        Driver dejanMatic = new Driver(new UserDto(null, "Dejan", "Matic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/dejanmaticprofilepicture.jpg")), "003817372222", "dejan.matic@email.com", "Nevidjena adresa", "$2a$12$LdmfGdNXFAPQjSZDTv4gHuE2UmqsjU5f4ZfTFFIzM/7HY1j67TJhS"));  // Burma123
        dejanMatic.setEnabled(true);
        dejanMatic.setVehicle(new Vehicle(null, "BMW X2", "PGAB111", 5, true, true, luxuryVehicleType, dejanMatic, new Location(null, "Prime Training Facility, Svetozara Miletića, Novi Sad, Srbija", 45.257847680306085f, 19.843377640699245f)));
        dejanMatic.setWorkHours(Set.of(new WorkHour(null, dejanMatic, LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)), new WorkHour(null, dejanMatic, LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)), new WorkHour(null, dejanMatic, LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)), new WorkHour(null, dejanMatic, LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)), new WorkHour(null, dejanMatic, LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
        dejanMatic.setRoles(List.of(driverRole));
        driverRepository.save(dejanMatic);
        //
        //
        // driver creation

        // passenger creation
        //
        //
        Passenger passenger1 = new Passenger(new UserDto(null, "Petar", "Petrovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger1.jpg")), "003817372727", "petar.petrovic@email.com", "Petrova adresa", "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123
        passenger1.setRoles(List.of(passengerRole));
        passenger1.setEnabled(true);
        passengerRepository.save(passenger1);

        Passenger passenger2 = new Passenger(new UserDto(null, "Jovan", "Jovanovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger2.jpg")), "003817379278", "jovan.jovanovic@email.com", "Jovanova adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passenger2.setRoles(List.of(passengerRole));
        passenger2.setEnabled(true);
        passengerRepository.save(passenger2);

        Passenger passenger3 = new Passenger(new UserDto(null, "Mirko", "Mirkovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger3.jpg")), "00381737111", "mirko.mirkovic@email.com", "Mirkova adresa", "$2a$12$nYULTJpydL5pFRSxQ30DnOlhu/m/O6U4CoWLqea82PYKNsswHCEsG"));  // Mirko123
        passenger3.setRoles(List.of(passengerRole));
        passenger3.setEnabled(true);
        passengerRepository.save(passenger3);

        Passenger passenger4 = new Passenger(new UserDto(null, "Aleksandar", "Popovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger4.jpg")), "0038173724234", "aleksandar.popovic@email.com", "Aleksandrova adresa", "$2a$12$yNYY1KoO4DDFSLzqRBXPJ.EJU3us/O8ws5d45pQ856BoiS70mslyu"));  // Aleksandar123
        passenger4.setRoles(List.of(passengerRole));
        passenger4.setEnabled(true);
        passengerRepository.save(passenger4);

        Passenger passenger5 = new Passenger(new UserDto(null, "Vuk", "Perisic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger5.jpg")), "003827372727", "vuk.perisic@email.com", "Vukova adresa", "$2a$12$fCHBcs11T5oI78NcRModiuCn5TjFE1QLn9x1awvf0meSO7LqSepc2"));  // Vuk12345
        passenger5.setRoles(List.of(passengerRole));
        passenger5.setEnabled(true);
        passengerRepository.save(passenger5);

        Passenger passenger6 = new Passenger(new UserDto(null, "Mitar", "Mitrovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger6.jpg")), "003812372727", "mitar.mitrovic@email.com", "Mitrova adresa", "$2a$12$8gG3n5awIfoIWc0DGuATb.7kLzzhxjAtkirQe7Aei0F6j7agjyYwa")); // Mitar123
        passenger6.setRoles(List.of(passengerRole));
        passenger6.setEnabled(true);
        passengerRepository.save(passenger6);

        Passenger passenger7 = new Passenger(new UserDto(null, "Marko", "Markovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger7.jpg")), "003817349278", "marko.markovic@email.com", "Markova adresa", "$2a$12$X2hJLrw8EIxWrahtYNVVyutfLy0DaHWAke36mffA4cQbOXTTYvDd6"));  // Marko123
        passenger7.setRoles(List.of(passengerRole));
        passenger7.setEnabled(true);
        passengerRepository.save(passenger7);

        Passenger passenger8 = new Passenger(new UserDto(null, "Sandra", "Sandric", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger8.jpg")), "00381737119", "sandra.sandric@email.com", "Sandrina adresa", "$2a$12$HRhVDOTZUv5l7LqTbw0OpewCzzJ.0KcwvtLLIEJRr.6xMfdq7pyj6"));  // Sandra123
        passenger8.setRoles(List.of(passengerRole));
        passenger8.setEnabled(true);
        passengerRepository.save(passenger8);

        Passenger passenger9 = new Passenger(new UserDto(null, "Jelena", "Jelenic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger9.jpg")), "0038172724234", "jelena.jelenic@email.com", "Jelenina adresa", "$2a$12$Hjvs0V.fzYWgsBoQlpnyZu5FdSUekzuQiSktCd4PkjwnazxpX/zOK"));  // Jelena123
        passenger9.setRoles(List.of(passengerRole));
        passenger9.setEnabled(true);
        passengerRepository.save(passenger9);

        Passenger passenger10 = new Passenger(new UserDto(null, "Bojana", "Bogdanovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/passenger10.jpg")), "003817373333", "bojana.bogdanovic@email.com", "Bojanina adresa", "$2a$12$YURa0Fx5M.MU3lSN1H7os.TI62diTbuQwlAZCdaudAJAIebFZHt/O"));  // Bojana123
        passenger10.setRoles(List.of(passengerRole));
        passenger10.setEnabled(true);
        passengerRepository.save(passenger10);

        Passenger passenger11 = new Passenger(new UserDto(null, "Rada", "Manojlovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/radamanojlovicprofilepicture.jpg")), "003817396933", "rada.manojlovic@email.com", "Nisam kriva sto sam kao vatra ziva. Nikako, nikako.", "$2a$12$e73GZnW5/D3AGR48li6aeeNLncvg78GSzV79F77XgKhG2BL9mKrey"));  // Vatraziva123
        passenger11.setRoles(List.of(passengerRole));
        passenger11.setEnabled(true);
        passengerRepository.save(passenger11);

        Passenger passenger12 = new Passenger(new UserDto(null, "Aleksandra", "Prijovic", DatatypeConverter.printBase64Binary(Constants.pictureStringToBytes("src/main/resources/static/aleksandraprijovicprofilepicture.jpg")), "003817373555", "aleksandra.prijovic@email.com", "Ko si ti", "$2a$12$/FyM8wqH0/XcOZhIqrNLN.Yg9NcH4GqyeaPRdmK.NDss9Y2Sb39wS"));  // Legitimno123
        passenger12.setRoles(List.of(passengerRole));
        passenger12.setEnabled(true);
        passengerRepository.save(passenger12);


        List<Passenger> passengers = List.of(passenger1, passenger2, passenger3, passenger4, passenger5, passenger6, passenger7, passenger8, passenger9, passenger10, passenger11, passenger12);
        //
        //
        // passenger creation

        // declaring routes that will be saved when the ride that contains them gets created
        //
        //
        Route route1 = new Route(null, 1000, 3, new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija", 45.24914205013315f, 19.843100056994654f), new Location(null, "Srpsko narodno pozorište, Pozorišni trg, Novi Sad, Srbija", 45.25510777309239f, 19.842949154190308f));
        Route route2 = new Route(null, 1900, 5, new Location(null, "Katolicka Porta 4, Novi Sad", 45.25642044682303f, 19.845453240699275f), new Location(null, "Dunavski Park, Dunavska, Novi Sad, Srbija", 45.25539880319645f, 19.85058802720829f));
        Route route3 = new Route(null, 3600, 9, new Location(null, "NTP NS, Fruškogorska, Novi Sad, Serbia", 45.24475880747688f, 19.84708251359552f), new Location(null, "Knin, Novi Sad, Serbia", 45.25433739645546f, 19.820878211862986f));
        Route route4 = new Route(null, 6700, 10, new Location(null, "Prime Training Facility, Svetozara Miletića, Novi Sad, Srbija", 45.257847680306085f, 19.843377640699245f), new Location(null, "Police Academy, Železnička, Sremska Kamenica, Srbija", 45.22163647056052f, 19.850156546431652f));
        Route route5 = new Route(null, 5400, 11, new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija", 45.24914205013315f, 19.843100056994654f), new Location(null, "Sportski Centar “Slana Bara”, Sentandrejski put, Novi Sad, Srbija", 45.29135042557076f, 19.824244340624777f));
        Route route6 = new Route(null, 3600, 9, new Location(null, "Lidl, Temerinska 98A, Novi Sad 400706", 45.27209773331658f, 19.843144506684034f), new Location(null, "TOČIONICA Detelinara, Hadži Ruvimova 49, Novi Sad", 45.25663257309855f, 19.814734548340745f));
        Route route7 = new Route(null, 4200, 9, new Location(null, "Kozmetički salon Ceca Skin Care Novi Sad, Draška Ređepa 6, Novi Sad 21000", 45.26074361074408f, 19.79649207161171f), new Location(null, "Formax Store, 21000, Novi Sad, Južno-bački okrug, Cara Dušana 47, Novi Sad 21000", 45.244036506388284f, 19.825288268059374f));
        Route route8 = new Route(null, 9000, 16, new Location(null, "Lidl, Temerinska 98A, Novi Sad 400706", 45.271563834606305f, 19.84308115088968f), new Location(null, "Svečana sala Kristal, Ive Lole Ribara 4a, Veternik", 45.24995431340382f, 19.751432512138383f));
        Route route9 = new Route(null, 11200, 19, new Location(null, "Hotel „Sole mio“, Sentandrejski put 165, Novi Sad 21000", 45.311287992288996f, 19.824607343808882f), new Location(null, "Castello Luxury Spa, Novi Sad", 45.24239236945359f, 19.792662991633424f));
        Route route10 = new Route(null, 14400, 24, new Location(null, "Градско Гробље, Novi Sad", 45.274077132923246f, 19.792574980028654f), new Location(null, "Kafana Tako Je Suđeno, Ribnjak gornji put, Petrovaradin", 45.23220418666553f, 19.856089690524232f));

        List<Route> routes = List.of(route1, route2, route3, route4, route5, route6, route7, route8, route9, route10);
        //
        //
        // route declarations

        // ride creation
        //
        //

        // finished rides
        //
        //
        final int RIDE_COUNT = 300;
        List<Ride> rides = new ArrayList<>();
        LocalDateTime sasaStartRide = LocalDateTime.of(2022, Month.OCTOBER, 3, 8, 5);
        LocalDateTime dejanStartRide = LocalDateTime.of(2022, Month.OCTOBER, 5, 9, 32);
        for (int i = 0; i < RIDE_COUNT; ++i) {
            Route sasaRoute = routes.get(random.nextInt(routes.size())).clone();
            Route dejanRoute = routes.get(random.nextInt(routes.size())).clone();
            Set<Passenger> sasaPassengers = new HashSet<>();
            Set<Passenger> dejanPassengers = new HashSet<>();
            for (int j = 0; j < 6; j += 2) {
                if (random.nextBoolean()) {
                    int passengerIndex = random.nextBoolean() ? j : j + 1;
                    sasaPassengers.add(passengers.get(passengerIndex));
                }
            }
            if (sasaPassengers.isEmpty()) {
                sasaPassengers.add(passenger1);
            }
            for (int j = 6; j < 12; j += 2) {
                if (random.nextBoolean()) {
                    int passengerIndex = random.nextBoolean() ? j : j + 1;
                    dejanPassengers.add(passengers.get(passengerIndex));
                }
            }
            if (dejanPassengers.isEmpty()) {
                dejanPassengers.add(passenger11);
            }
            Ride rideSasa = rideRepository.save(
                    new Ride(
                            null,
                            sasaMatic.getVehicle().getVehicleType().getPricePerKm() + sasaRoute.getDistanceInMeters() / 1000 * 120,
                            sasaStartRide,
                            sasaStartRide.plusMinutes(sasaRoute.getEstimatedTimeInMinutes()),
                            sasaRoute.getEstimatedTimeInMinutes(),
                            sasaMatic.getVehicle().isBabyAllowed(),
                            sasaMatic.getVehicle().isPetsAllowed(),
                            false,
                            Enums.RideStatus.FINISHED,
                            sasaMatic,
                            sasaMatic.getVehicle().getVehicleType(),
                            sasaPassengers,
                            null,
                            List.of(sasaRoute)
                    )
            );
            Ride rideDejan = rideRepository.save(
                    new Ride(
                            null,
                            dejanMatic.getVehicle().getVehicleType().getPricePerKm() + dejanRoute.getDistanceInMeters() / 1000 * 120,
                            dejanStartRide,
                            dejanStartRide.plusMinutes(dejanRoute.getEstimatedTimeInMinutes()),
                            dejanRoute.getEstimatedTimeInMinutes(),
                            dejanMatic.getVehicle().isBabyAllowed(),
                            dejanMatic.getVehicle().isPetsAllowed(),
                            false,
                            Enums.RideStatus.FINISHED,
                            dejanMatic,
                            dejanMatic.getVehicle().getVehicleType(),
                            dejanPassengers,
                            null,
                            List.of(dejanRoute)
                    )
            );

            sasaStartRide = sasaStartRide.plusMinutes(sasaRoute.getEstimatedTimeInMinutes() + random.nextInt(30, 120));
            dejanStartRide = dejanStartRide.plusMinutes(sasaRoute.getEstimatedTimeInMinutes() + random.nextInt(30, 120));
            rides.add(rideSasa);
            rides.add(rideDejan);

            // this piece of code check if the startRide is on the weekend and if it is, it startRide goes to next monday
            if (sasaStartRide.getHour() >= 16) {
                sasaStartRide = LocalDateTime.of(sasaStartRide.toLocalDate().plusDays(1), LocalTime.of(8, 30));
                if (sasaStartRide.getDayOfWeek() == (DayOfWeek.SATURDAY)) {
                    sasaStartRide = LocalDateTime.of(sasaStartRide.toLocalDate().plusDays(2), LocalTime.of(8, 30));
                }
            }
            if (dejanStartRide.getHour() >= 16) {
                dejanStartRide = LocalDateTime.of(dejanStartRide.toLocalDate().plusDays(1), LocalTime.of(8, 30));
                if (dejanStartRide.getDayOfWeek() == (DayOfWeek.SATURDAY)) {
                    dejanStartRide = LocalDateTime.of(dejanStartRide.toLocalDate().plusDays(2), LocalTime.of(8, 30));
                }
            }

        }
        //
        //
        // finished rides

        // accepted rides
        //
        //
        sasaStartRide = LocalDateTime.now().plusMinutes(60);
        dejanStartRide = LocalDateTime.now().plusMinutes(70);
        for (int i = 0; i < 5; ++i) {
            Route sasaRoute = routes.get(random.nextInt(routes.size())).clone();
            Route dejanRoute = routes.get(random.nextInt(routes.size())).clone();
            Set<Passenger> sasaPassengers = new HashSet<>();
            Set<Passenger> dejanPassengers = new HashSet<>();
            for (int j = 0; j < 6; j += 2) {
                if (random.nextBoolean()) {
                    int passengerIndex = random.nextBoolean() ? j : j + 1;
                    sasaPassengers.add(passengers.get(passengerIndex));
                }
            }
            if (sasaPassengers.isEmpty()) {
                sasaPassengers.add(passenger1);
            }
            for (int j = 6; j < 12; j += 2) {
                if (random.nextBoolean()) {
                    int passengerIndex = random.nextBoolean() ? j : j + 1;
                    dejanPassengers.add(passengers.get(passengerIndex));
                }
            }
            if (dejanPassengers.isEmpty()) {
                dejanPassengers.add(passenger11);
            }
            rideRepository.save(
                    new Ride(
                            null,
                            sasaMatic.getVehicle().getVehicleType().getPricePerKm() + sasaRoute.getDistanceInMeters() / 1000 * 120,
                            sasaStartRide,
                            null,
                            sasaRoute.getEstimatedTimeInMinutes(),
                            sasaMatic.getVehicle().isBabyAllowed(),
                            sasaMatic.getVehicle().isPetsAllowed(),
                            false,
                            Enums.RideStatus.ACCEPTED,
                            sasaMatic,
                            sasaMatic.getVehicle().getVehicleType(),
                            sasaPassengers,
                            null,
                            List.of(sasaRoute)
                    )
            );
            rideRepository.save(
                    new Ride(
                            null,
                            dejanMatic.getVehicle().getVehicleType().getPricePerKm() + dejanRoute.getDistanceInMeters() / 1000 * 120,
                            dejanStartRide,
                            null,
                            dejanRoute.getEstimatedTimeInMinutes(),
                            dejanMatic.getVehicle().isBabyAllowed(),
                            dejanMatic.getVehicle().isPetsAllowed(),
                            false,
                            Enums.RideStatus.ACCEPTED,
                            dejanMatic,
                            dejanMatic.getVehicle().getVehicleType(),
                            dejanPassengers,
                            null,
                            List.of(dejanRoute)
                    )
            );
            sasaStartRide = sasaStartRide.plusMinutes(sasaRoute.getEstimatedTimeInMinutes() + random.nextInt(15, 30));
            dejanStartRide = dejanStartRide.plusMinutes(sasaRoute.getEstimatedTimeInMinutes() + random.nextInt(15, 30));
        }
        //
        //
        // accepted rides

        //
        //
        // ride creation


        // creating vehicles
        //
        //
        vehicleRepository.save(new Vehicle(null, "BMW iXM60", "PGAA112", 5, false, false, standardVehicleType, null, new Location(null, "Valentina Vodnika 10, Novi Sad", 45.255956f, 19.8366902f)));
        vehicleRepository.save(new Vehicle(null, "BMW iX3", "PGAA113", 5, true, true, standardVehicleType, null, new Location(null, "Beogradska 7, Petrovaradin", 45.254896f, 19.8612956f)));
        //
        //
        // creating vehicles

        // panic creation
        //
        //
        Ride rideToPanic = rides.get(random.nextInt(rides.size()));
        panicRepository.save(new Panic(null, rideToPanic.getStartTime().plusMinutes(3), false, "Driver is very uncomfortable and is making racist comments", rideToPanic, passenger9));
        //
        //
        // panic creation


        // review creation
        //
        //
        final String VERY_POSITIVE_DRIVER_COMMENT = "This driver exceeded my expectations! He got us there on time and the ride was very pleasant. Well done!";
        final String POSITIVE_DRIVER_COMMENT = "This ride was great. I enjoyed it";
        final String NEUTRAL_DRIVER_COMMENT = "This driver was solid. The ride felt nice.";
        final String NEGATIVE_DRIVER_COMMENT = "Not very good. Wasn't very pleasant but at least we arrived at the destination on timed.";
        final String CATASTROPHIC_DRIVER_COMMENT = "Awful! I don't think there is a law that this driver didn't break. That was very unpleasant and I might sue you.";
        Map<Integer, String> driverCommentForRating = new HashMap<>();
        driverCommentForRating.put(5, VERY_POSITIVE_DRIVER_COMMENT);
        driverCommentForRating.put(4, POSITIVE_DRIVER_COMMENT);
        driverCommentForRating.put(3, NEUTRAL_DRIVER_COMMENT);
        driverCommentForRating.put(2, NEGATIVE_DRIVER_COMMENT);
        driverCommentForRating.put(1, CATASTROPHIC_DRIVER_COMMENT);

        final String VERY_POSITIVE_VEHICLE_COMMENT = "This might be the cleanest car I ever drove in. As soon as I entered I could feel a very pleasant smell. Props to the driver, this vehicle is preserved amazingly.";
        final String POSITIVE_VEHICLE_COMMENT = "It was very clean and the car looks amazing. Is it a new model";
        final String NEUTRAL_VEHICLE_COMMENT = "I have nothing bad or good to say about the vehicle besides that it got the job done.";
        final String NEGATIVE_VEHICLE_COMMENT = "There were crumbs on the seats and the inside didn't smell very nice... Wouldn't recommend";
        final String CATASTROPHIC_VEHICLE_COMMENT = "I don't know how I managed to not to throw up during the ride. Absolutely disgusting. This shouldn't be legal.";
        Map<Integer, String> vehicleCommentForRating = new HashMap<>();
        vehicleCommentForRating.put(5, VERY_POSITIVE_VEHICLE_COMMENT);
        vehicleCommentForRating.put(4, POSITIVE_VEHICLE_COMMENT);
        vehicleCommentForRating.put(3, NEUTRAL_VEHICLE_COMMENT);
        vehicleCommentForRating.put(2, NEGATIVE_VEHICLE_COMMENT);
        vehicleCommentForRating.put(1, CATASTROPHIC_VEHICLE_COMMENT);

        for (Ride ride : rides) {
            for (Passenger passenger : ride.getPassengers()) {
                if (random.nextBoolean()) {
                    int driverRating = random.nextInt(1, 6);
                    DriverReview driverReview = new DriverReview();
                    driverReview.setRating(driverRating);
                    driverReview.setComment(driverCommentForRating.get(driverRating));
                    driverReview.setRide(ride);
                    driverReview.setPassenger(passenger);
                    driverReview.setDriver(ride.getDriver());

                    int vehicleRating = random.nextInt(1, 6);
                    VehicleReview vehicleReview = new VehicleReview();
                    vehicleReview.setRating(vehicleRating);
                    vehicleReview.setComment(vehicleCommentForRating.get(vehicleRating));
                    vehicleReview.setRide(ride);
                    vehicleReview.setPassenger(passenger);
                    vehicleReview.setVehicle(ride.getDriver().getVehicle());

                    driverReviewRepository.save(driverReview);
                    vehicleReviewRepository.save(vehicleReview);
                }
            }
        }
        //
        //
        // review creation
    }

}




