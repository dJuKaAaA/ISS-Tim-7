package com.tim7.iss.tim7iss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tim7.iss.tim7iss.dto.RideDto;
import com.tim7.iss.tim7iss.dto.UserDto;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import com.tim7.iss.tim7iss.services.RideService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RideControllerTests.class)
public class RideControllerTests {

    //  RideDetails
    @MockBean
    private RideService rideService;




    @Autowired
    private MockMvc mockMvc;

    private String driverToken = "";

    private String passengerToken = "";

    private Admin getAdmin() throws IOException {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setFirstName("Adonis");
        admin.setLastName("Adonis");
        admin.setProfilePicture(DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()));
        admin.setPhoneNumber("003814523423");
        admin.setEmailAddress("admin@email.com");
        admin.setPassword("$2a$12$c9cKc9F6WaOKIchi9bWCpOrWRnXTBEKTU4NFtS3azXhJWy4TAcTey");
        return admin;
    }

    private Passenger getPassenger() throws IOException {
        Passenger passenger = new Passenger(new UserDto(3L, "Petar", "Petrovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372727", "petar.petrovic@email.com", "Petrova adresa", "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123

        return passenger;
    }

    private Driver getDriver() throws IOException {
        VehicleType vehicleType = new VehicleType(null, 100, "STANDARD");

        Driver driver = new Driver(new UserDto(2L, "Mika", "Mikic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "mika.mikic@email.com", "Mikina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
        driver.setEnabled(true);

        // vehicle
        driver.setVehicle(new Vehicle(null, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));

        return driver;
    }

    private Route getRoute() {
        Route route = new Route(null, 1000, 3, new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija", 45.24914205013315f, 19.843100056994654f), new Location(null, "Srpsko narodno pozorište, Pozorišni trg, Novi Sad, Srbija", 45.25510777309239f, 19.842949154190308f));
        return route;
    }

    private Ride getFinishedRide() throws IOException {

        Driver driver = getDriver();
        Passenger passenger = getPassenger();
        Route route = getRoute();

        Ride ride = new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 8, 0), LocalDateTime.of(2023, Month.JANUARY, 19, 8, 20), route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.FINISHED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));
        return ride;
    }

    private Ride getRejectedRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger();
        Route route = getRoute();

        Ride ride = new Ride(null, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 8, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.REJECTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));
        return ride;
    }

    private Ride getAcceptedRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger();
        Route route = getRoute();

        Ride ride = new Ride(null, 1000, LocalDateTime.now().minusMinutes(10), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));
        return ride;
    }

    private Ride getPendingRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger();
        Route route = getRoute();

        Ride ride = new Ride(null, 1000, LocalDateTime.now().minusMinutes(10), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));
        return ride;
    }

    private Ride getActiveRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger();
        Route route = getRoute();

        Ride ride = new Ride(null, 1000, LocalDateTime.now().minusMinutes(10), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACTIVE, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));
        return ride;
    }

    private void driverLogin(){

    }


    @Test
    public void getRideById_shouldReturnRides() throws Exception {
        Driver driver = getDriver();
        Ride finishedRide = getFinishedRide();
        RideDto finishedRideDto = new RideDto(finishedRide);
        Mockito.when(rideService.getRideById(Mockito.any(Long.class))).thenReturn(finishedRideDto);

//        MvcResult result = mockMvc.perform(get("/api/ride/1").header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk())
//                .andReturn();

//        ObjectMapper objectMapper = new ObjectMapper();
//        RideDto rideDto = objectMapper.readValue(result.getResponse().getContentAsString(), RideDto.class);
    }
//    Delete existing favorite ride
//    End the ride

}
