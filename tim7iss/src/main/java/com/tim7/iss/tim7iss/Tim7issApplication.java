package com.tim7.iss.tim7iss;

import com.tim7.iss.tim7iss.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

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
        
        };
    }
}