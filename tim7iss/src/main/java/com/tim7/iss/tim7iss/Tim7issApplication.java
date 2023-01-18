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
            // menjana adresa
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


            // Menjana adresa
            // driver creation
            Driver driver = new Driver(new UserDto(null, "Mika", "Mikic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "driver@gmail.com", "Mikina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
            driver.setActive(true);
            driver.setEnabled(true);
            driver.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));
            driver.setWorkHours(Set.of(new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 19, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 19, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 20, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 20, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 21, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 21, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 22, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 22, 16, 0)), new WorkHour(null, driver, LocalDateTime.of(2022, Month.DECEMBER, 23, 8, 0), LocalDateTime.of(2022, Month.DECEMBER, 23, 16, 0))));
            driver.setRoles(List.of(driverRole));
            driver.setActive(true);
            driverRepository.save(driver);

            // passenger creation
            Passenger passenger1 = new Passenger(new UserDto(null, "Petar", "Petrovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372727", "passenger@gmail.com", "Petrova adresa", "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123
            passenger1.setRoles(List.of(passengerRole));
            passenger1.setEnabled(true);
            passengerRepository.save(passenger1);


        };
    }
}
