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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootApplication
public class Tim7issApplication {

    @Autowired
    private VehicleReviewRepository vehicleReviewRepository;
    @Autowired
    private PanicRepository panicRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DocumentRepository documentRepository;
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
    private RoutesRepository routeRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private UserActivationRepository userActivationRepository;
    @Autowired
    private FavoriteLocationRepository favoriteLocationRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Tim7issApplication.class, args);
    }

    // DISCLAIMER: for testing purposes
    @Bean
    protected InitializingBean sendDatabase() {
        Random random = new Random();
        return () -> {
            // vehicle type creation
            VehicleType vehicleType = vehicleTypeRepository.save(new VehicleType(null, 100, "STANDARD"));

            // role creation
            Role adminRole = roleRepository.save(new Role(null, "ROLE_ADMIN"));
            Role driverRole = roleRepository.save(new Role(null, "ROLE_DRIVER"));
            Role passengerRole = roleRepository.save(new Role(null, "ROLE_PASSENGER"));
            Role anonymousRole = roleRepository.save(new Role(null, "ROLE_ANONYMOUS"));

            // admin creatioAdmin123n
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
            Driver driver = new Driver(new UserDto(
                    null,
                    "Mika",
                    "Mikic",
                    DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()),
                    "003817372222",
                    "mika.mikic@email.com",
                    "Mikina adresa",
                    "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
            driver.setEnabled(true);
            driver.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5,
                    false, true, vehicleType, driver,
                    new Location(null,
                            "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad",
                            45.24648813f, 19.8516641f)));
            driver.setWorkHours(Set.of(
                    new WorkHour(null, driver,
                            LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0),
                            LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)),
                    new WorkHour(null, driver,
                            LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0),
                            LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)),
                    new WorkHour(null, driver,
                            LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0),
                            LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)),
                    new WorkHour(null, driver,
                            LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0),
                            LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)),
                    new WorkHour(null, driver,
                            LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0),
                            LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
            driver.setRoles(List.of(driverRole));
            driver.setActive(true);
            driverRepository.save(driver);

            // passenger creation
            Passenger passenger1 = new Passenger(new UserDto(
                    null,
                    "Petar",
                    "Petrovic",
                    DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()),
                    "003817372727",
                    "petar.petrovic@email.com",
                    "Petrova adresa",
                    "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123
            passenger1.setRoles(List.of(passengerRole));
            passenger1.setEnabled(true);
            passengerRepository.save(passenger1);
            Passenger passenger2 = new Passenger(new UserDto(
                    null,
                    "Jovan",
                    "Jovanovic",
                    DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()),
                    "003817379278",
                    "jovan.jovanovic@email.com",
                    "Jovanova adresa",
                    "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
            passenger2.setRoles(List.of(passengerRole));
            passenger2.setEnabled(true);
            passengerRepository.save(passenger2);
            Passenger passenger3 = new Passenger(new UserDto(
                    null,
                    "Mirko",
                    "Mirkovic",
                    DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()),
                    "00381737111",
                    "mirko.mirkovic@email.com",
                    "Mirkova adresa",
                    "$2a$12$nYULTJpydL5pFRSxQ30DnOlhu/m/O6U4CoWLqea82PYKNsswHCEsG"));  // Mirko123
            passenger3.setRoles(List.of(passengerRole));
            passenger3.setEnabled(true);
            passengerRepository.save(passenger3);
            Passenger passenger4 = new Passenger(new UserDto(
                    null,
                    "Aleksandar",
                    "Popovic",
                    DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()),
                    "0038173724234",
                    "aleksandar.popovic@email.com",
                    "Aleksandrova adresa",
                    "$2a$12$yNYY1KoO4DDFSLzqRBXPJ.EJU3us/O8ws5d45pQ856BoiS70mslyu"));  // Aleksandar123
            passenger4.setRoles(List.of(passengerRole));
            passenger4.setEnabled(true);
            passengerRepository.save(passenger4);
            Passenger passenger5 = new Passenger(new UserDto(
                    null,
                    "Vuk",
                    "Perisic",
                    DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()),
                    "003817372727",
                    "vuk.perisic@email.com",
                    "Vukova adresa",
                    "$2a$12$fCHBcs11T5oI78NcRModiuCn5TjFE1QLn9x1awvf0meSO7LqSepc2"));  // Vuk12345
            passenger5.setRoles(List.of(passengerRole));
            passenger5.setEnabled(true);
            passengerRepository.save(passenger5);


            // declaring routes that will be saved when the ride that contains them gets created
            Route route1 = new Route(null, 1000, 3,
                    new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija",
                            45.24914205013315f, 19.843100056994654f),
                    new Location(null, "Srpsko narodno pozorište, Pozorišni trg, Novi Sad, Srbija",
                            45.25510777309239f, 19.842949154190308f));
            Route route2 = new Route(null, 1900, 5,
                    new Location(null, "Katolicka Porta 4, Novi Sad",
                            45.25642044682303f, 19.845453240699275f),
                    new Location(null, "Dunavski Park, Dunavska, Novi Sad, Srbija",
                            45.25539880319645f, 19.85058802720829f));
            Route route3 = new Route(null, 3600, 9,
                    new Location(null, "NTP NS, Fruškogorska, Novi Sad, Serbia",
                            45.24475880747688f, 19.84708251359552f),
                    new Location(null, "Knin, Novi Sad, Serbia",
                            45.25433739645546f, 19.820878211862986f));
            Route route4 = new Route(null, 6700, 10,
                    new Location(null, "Prime Training Facility, Svetozara Miletića, Novi Sad, Srbija",
                            45.257847680306085f, 19.843377640699245f),
                    new Location(null, "Police Academy, Železnička, Sremska Kamenica, Srbija",
                            45.22163647056052f, 19.850156546431652f));
            Route route5 = new Route(null, 5400, 11,
                    new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija",
                            45.24914205013315f, 19.843100056994654f),
                    new Location(null, "Sportski Centar “Slana Bara”, Sentandrejski put, Novi Sad, Srbija",
                            45.29135042557076f, 19.824244340624777f));

            // ride creation
            // TODO: Make at least 20
            // TODO: Place the right price depending on the vehicle type and route distance
            // TODO: Place the right ride length
            Ride ride1 = rideRepository.save(new Ride(null, 1000,
                    LocalDateTime.now().plusMinutes(random.nextInt(60, 1400)),
                    null,
                    route1.getEstimatedTimeInMinutes(), false && driver.getVehicle().isBabyAllowed(),
                    true && driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED,
                    driver, driver.getVehicle().getVehicleType(),
                    Set.of(passenger1, passenger2, passenger3), null, List.of(route1.clone())));
            Ride ride2 = rideRepository.save(new Ride(null, 1000,
                    LocalDateTime.now().plusMinutes(random.nextInt(1500, 2800)),
                    null,
                    route3.getEstimatedTimeInMinutes(), false && driver.getVehicle().isBabyAllowed(),
                    true && driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.FINISHED,
                    driver, driver.getVehicle().getVehicleType(),
                    Set.of(passenger4, passenger5), null, List.of(route3.clone())));
            Ride ride3 = rideRepository.save(new Ride(null, 1000,
                    LocalDateTime.now().plusMinutes(30),
                    null,
                    route4.getEstimatedTimeInMinutes(), false && driver.getVehicle().isBabyAllowed(),
                    true && driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED,
                    driver, driver.getVehicle().getVehicleType(),
                    Set.of(passenger2, passenger3, passenger5), null, List.of(route4.clone())));
            Ride ride4 = rideRepository.save(new Ride(null, 1000,
                    LocalDateTime.of(2022, Month.OCTOBER, 1, 11, 23),
                    LocalDateTime.of(2022, Month.OCTOBER, 1, 12, 1),
                    route2.getEstimatedTimeInMinutes(), true && driver.getVehicle().isBabyAllowed(),
                    false && driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED,
                    driver, driver.getVehicle().getVehicleType(),
                    Set.of(passenger1, passenger2, passenger4), null, List.of(route2.clone())));
            Ride ride5 = rideRepository.save(new Ride(null, 1000,
                    LocalDateTime.now().plusMinutes(random.nextInt(2900, 4200)),
                    null,
                    route5.getEstimatedTimeInMinutes(), false && driver.getVehicle().isBabyAllowed(),
                    false && driver.getVehicle().isPetsAllowed(), true, Enums.RideStatus.ACCEPTED,
                    driver, driver.getVehicle().getVehicleType(),
                    Set.of(passenger1, passenger3, passenger5), null, List.of(route5.clone())));
            Ride ride6 = rideRepository.save(new Ride(null, 1000,
                    LocalDateTime.of(2022, Month.NOVEMBER, 1, 9, 3),
                    LocalDateTime.of(2022, Month.NOVEMBER, 1, 9, 25),
                    route5.getEstimatedTimeInMinutes(), false && driver.getVehicle().isBabyAllowed(),
                    true && driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.FINISHED,
                    driver, driver.getVehicle().getVehicleType(),
                    Set.of(passenger5), null, List.of(route5.clone())));
            Ride ride7 = rideRepository.save(new Ride(null, 1000,
                    LocalDateTime.now().plusMinutes(random.nextInt(4300, 6000)),
                    null,
                    route3.getEstimatedTimeInMinutes(), true && driver.getVehicle().isBabyAllowed(),
                    true && driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED,
                    driver, driver.getVehicle().getVehicleType(),
                    Set.of(passenger4), null, List.of(route3.clone())));

            // creating vehicles
            locationRepository.save(new Location(null, "Valentina Vodnika 10, Novi Sad", 45.255956f, 19.8366902f));
            locationRepository.save(new Location(null, "Beogradska 7, Petrovaradin", 45.254896f, 19.8612956f));
            vehicleRepository.save(new Vehicle(null, "BMW iXM60", "PGAA112", 5,
                    false, false, vehicleType, null,
                    new Location(null, "Valentina Vodnika 10, Novi Sad", 45.255956f, 19.8366902f)));
            vehicleRepository.save(new Vehicle(null, "BMW iX3", "PGAA113", 5,
                    true, true, vehicleType, null,
                    new Location(null, "Beogradska 7, Petrovaradin", 45.254896f, 19.8612956f)));

            // creating user activation entities
            userActivationRepository.save(new UserActivation(null,
                    LocalDateTime.of(2022, Month.DECEMBER, 21, 21, 0),
                    LocalDateTime.of(2023, Month.FEBRUARY, 1, 23, 59),
                    passenger1));
            userActivationRepository.save(new UserActivation(null,
                    LocalDateTime.of(2021, Month.DECEMBER, 21, 21, 0),
                    LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 59),
                    passenger2));
//            userActivationRepository.save(new UserActivation(null,
//                    LocalDateTime.of(2022, Month.DECEMBER, 21, 21, 0),
//                    LocalDateTime.of(2023, Month.FEBRUARY, 1, 23, 59),
//                    passenger3));
//            userActivationRepository.save(new UserActivation(null,
//                    LocalDateTime.of(2022, Month.DECEMBER, 21, 21, 0),
//                    LocalDateTime.of(2023, Month.FEBRUARY, 1, 23, 59),
//                    passenger4));
//            userActivationRepository.save(new UserActivation(null,
//                    LocalDateTime.of(2022, Month.DECEMBER, 21, 21, 0),
//                    LocalDateTime.of(2023, Month.FEBRUARY, 1, 23, 59),
//                    passenger5));

            // panic creation
            // TODO: Enter valid date time according to ride start date
            panicRepository.save(new Panic(null, LocalDateTime.now(), false,
                    "Driver is very uncomfortable and is making racist comments", ride7,
                    passenger4));

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
            messageRepository.save(new Message(null,LocalDateTime.now(),"Ride","proba", driver, passenger2,ride4));
            messageRepository.save(new Message(null,LocalDateTime.now(),"Ride","proba", passenger3, driver,ride4));

//            FavoriteLocation favoriteLocation = new FavoriteLocation();
//            favoriteLocation.setPassengers(Set.of(passenger1,passenger2,passenger3));
//            favoriteLocation.setRoutes(Set.of(route1, route2, route3));
//            favoriteLocation.setVehicleType(vehicleType);
//            favoriteLocation.setPetTransport(false);
//            favoriteLocation.setBabyTransport(false);
//            favoriteLocationRepository.save(favoriteLocation);
        };
    }
}
