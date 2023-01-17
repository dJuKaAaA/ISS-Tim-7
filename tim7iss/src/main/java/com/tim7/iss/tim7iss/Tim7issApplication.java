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
            admin.setEmailAddress("admin@gmail.com");
            admin.setPassword("$2a$12$c9cKc9F6WaOKIchi9bWCpOrWRnXTBEKTU4NFtS3azXhJWy4TAcTey");  // Admin123
            admin.setEnabled(true);
            admin.setRoles(List.of(adminRole));
            adminRepository.save(admin);


            // driver creation
            Driver driver = new Driver(new UserDto(null, "Mika", "Mikic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "driver@gmail.com", "Mikina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
            driver.setActive(true);
            driver.setEnabled(true);
            driver.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));
            driver.setWorkHours(Set.of(new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
            driver.setRoles(List.of(driverRole));
            driverRepository.save(driver);

            // passenger creation
            Passenger passenger1 = new Passenger(new UserDto(null, "Petar", "Petrovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372727", "passenger@gmail.com", "Petrova adresa", "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123
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


            // creating vehicles
            locationRepository.save(new Location(null, "Valentina Vodnika 10, Novi Sad", 45.255956f, 19.8366902f));
            locationRepository.save(new Location(null, "Beogradska 7, Petrovaradin", 45.254896f, 19.8612956f));
            vehicleRepository.save(new Vehicle(null, "BMW iXM60", "PGAA112", 5, false, false, vehicleType, null, new Location(null, "Valentina Vodnika 10, Novi Sad", 45.255956f, 19.8366902f)));
            vehicleRepository.save(new Vehicle(null, "BMW iX3", "PGAA113", 5, true, true, vehicleType, null, new Location(null, "Beogradska 7, Petrovaradin", 45.254896f, 19.8612956f)));

        };
    }
}
