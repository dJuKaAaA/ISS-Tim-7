package com.tim7.iss.tim7iss;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverDocumentRequestRepository;
import com.tim7.iss.tim7iss.repositories.DriverRequestRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.services.RideService;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideControllerTests {


    @Autowired
    DriverDocumentRequestRepository driverDocumentRequestRepository;
    @Autowired
    DriverRequestRepository driverRequestRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;

    // Ovo se testira
    @Autowired
    private RideService rideService;
    @Autowired
    private MockMvc mockMvc;
    private String driverToken = "";
    private String passenger1Token = "";
    private String adminToken = "";
    @Autowired
    private RideRepository rideRepository;


    // Test podaci
    private Admin getAdmin() throws IOException {
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

    private Passenger getPassenger1() throws IOException {
        Passenger passenger1 = new Passenger(new UserDto(3L, "Petar", "Petrovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372727", "petar.petrovic@email.com", "Petrova adresa", "$2a$12$lA8WEWzn3E7l53E2HYpX3ee0q.ZOVDjY34jNYTs/n9ucvebpY3v86")); // Petar123
        passenger1.setId(3L);
        passenger1.setEnabled(true);

        return passenger1;
    }

    private Passenger getPassenger2() throws IOException {
        Passenger passenger2 = new Passenger(new UserDto(4L, "Jovan", "Jovanovic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817379278", "jovan.jovanovic@email.com", "Jovanova adresa", "$2a$12$pr0BMsJvyWNGiFuQmMQ.UeV8a7zvlv.m3m9nCVprTwcKBpe2iYJS."));  // Jovan123
        passenger2.setEnabled(true);
        passenger2.setId(4L);

        return passenger2;
    }

    private VehicleType getVehicleType() {
        VehicleType vehicleType = new VehicleType(1L, 100, "STANDARD");
        return vehicleType;
    }

    private Driver getDriver() throws IOException {
        VehicleType vehicleType = getVehicleType();

        Driver driver = new Driver(new UserDto(1L, "Mika", "Mikic", DatatypeConverter.printBase64Binary(Constants.getPlaceHolderProfilePicture()), "003817372222", "mika.mikic@email.com", "Mikina adresa", "$2a$12$4z3y3x45WYUdy98AhcW5Vee6UmIAClGcs61e1yJZpwpaobzkm5asa"));  // Mika1234
        driver.setEnabled(true);
        driver.setId(1L);


        driver.setVehicle(new Vehicle(1L, "BMW X2", "PGAA111", 5, false, true, vehicleType, driver, new Location(null, "Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad", 45.24648813f, 19.8516641f)));

        return driver;
    }

    // ID je null
    private Route getRoute() {
        Route route = new Route(null, 1000, 3, new Location(null, "The Camelot Novi Sad, Sremska, Novi Sad, Srbija", 45.24914205013315f, 19.843100056994654f), new Location(null, "Srpsko narodno pozoriste, Pozorisni trg, Novi Sad, Srbija", 45.25510777309239f, 19.842949154190308f));
        return route;
    }

    private Ride getPendingRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger1 = getPassenger1();
        Passenger passenger2 = getPassenger2();
        Route route = getRoute();
        route.setId(1L);

        Ride pendingRide = new Ride(1L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.PENDING, driver, driver.getVehicle().getVehicleType(), Set.of(passenger1, passenger2), null, List.of(route));

        return pendingRide;
    }

    private Ride getActiveRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(2L);

        Ride activeRide = new Ride(2L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACTIVE, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        return activeRide;
    }

    private Ride getFinishedRide() throws IOException {

        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(3L);

        Ride finishedRide = new Ride(3L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), LocalDateTime.of(2023, Month.JANUARY, 19, 16, 20), route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.FINISHED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        return finishedRide;
    }

    private Ride getRejectedRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(4L);

        Ride rejectedRide = new Ride(4L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.REJECTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        Refusal refusal = new Refusal(null, driver, "Refusal", LocalDateTime.now().minusMinutes(195), rejectedRide);
        rejectedRide.setRefusal(refusal);
        return rejectedRide;
    }

    private Ride getAcceptedRide() throws IOException {
        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        Route route = getRoute();
        route.setId(5L);

        Ride acceptedRide = new Ride(5L, 1000, LocalDateTime.of(2023, Month.JANUARY, 19, 16, 0), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        return acceptedRide;
    }

    private String login(String email, String password) {

        //Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();

        String jwt = tokenUtils.generateToken(user.getUsername());
        return jwt;
    }


    private FavoriteLocation getFavoriteLocation() throws IOException {
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

    private RideDto prepareRideDtoBeforeAssertion(RideDto rideDto) {
        List<UserRefDto> passengers = rideDto.getPassengers();
        passengers.sort(Comparator.comparing(UserRefDto::getId));
        rideDto.setPassengers(passengers);

        List<LocationForRideDto> locations = rideDto.getLocations();
        locations.sort(Comparator.comparing(LocationForRideDto::getDistanceInMeters));
        rideDto.setLocations(locations);

        return rideDto;
    }

    @BeforeAll
    void beforeAll() throws IOException {
        Admin admin = getAdmin();
        Driver driver = getDriver();
        Passenger passenger = getPassenger1();
        this.passenger1Token = login(passenger.getEmailAddress(), "Petar123");
        this.driverToken = login(driver.getEmailAddress(), "Mika1234");
        this.adminToken = login(admin.getEmailAddress(), "Admin123");
    }


    //  RideDetails

    @Test
    public void getRideById_shouldBeUnauthorized() throws Exception {
        int driverId = 3;
        String url = "/api/ride/" + driverId;
        String headerName = "Authorization";

        mockMvc.perform(get(url).header(headerName, "")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void getRideById_shouldThrowRideDoesNotExist() throws Exception {
        int rideId = 123;
        String url = "/api/ride/" + rideId;
        String headerName = "Authorization";
        String token = "Bearer " + driverToken;

        MvcResult result = mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void getRideById_shouldReturnRide() throws Exception {
        Ride finishedRide = getFinishedRide();
        int finishedRideId = Math.toIntExact(finishedRide.getId());
        String url = "/api/ride/" + finishedRideId;
        String headerName = "Authorization";
        String token = "Bearer " + driverToken;

        RideDto finishedRideDto = new RideDto(finishedRide);
        MvcResult result = mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto responseRideDto = objectMapper.readValue(result.getResponse().getContentAsString(), RideDto.class);

        prepareRideDtoBeforeAssertion(finishedRideDto);
        prepareRideDtoBeforeAssertion(responseRideDto);

        assertEquals(finishedRideDto, responseRideDto);
    }


    // AcceptRide
    @Test
    public void acceptRide_shouldBeUnauthorized() throws Exception {
        int rideId = 1;
        String url = "/api/ride/" + rideId + "/accept";
        String headerName = "Authorization";

        mockMvc.perform(put(url).header(headerName, "")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void acceptRide_shouldBeForbidden() throws Exception {
        int rideId = 1;
        String url = "/api/ride/" + rideId + "/accept";
        String headerName = "Authorization";
        String token = "Bearer " + passenger1Token;
        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();

    }

    @Test
    public void acceptRide_shouldThrowRideDoesNotExistWhenRideIdNotExist() throws Exception {
        int rideId = 123;
        String url = "/api/ride/" + rideId + "/accept";
        String headerName = "Authorization";
        String token = "Bearer " + driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void acceptRide_shouldThrowRideCancellationExceptionWhenRideStatusIsPending() throws Exception {
        Ride ride = getPendingRide();
        int rideId = Math.toIntExact(getPendingRide().getId());
        String url = "/api/ride/" + rideId + "/accept";
        String headerName = "Authorization";
        String token = "Bearer " + driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isBadRequest()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Cannot accept a ride that is not in status PENDING!", errorDto.getMessage());
    }

    @Test
    public void acceptRide_shouldAcceptRide() throws Exception {
        Ride pendingRide = getPendingRide();

        int pendingRideId = Math.toIntExact(pendingRide.getId());
        String url = "/api/ride/" + pendingRideId + "/accept";
        String headerName = "Authorization";
        String token = "Bearer " + driverToken;

        RideDto pendingRideDto = new RideDto(pendingRide);
        pendingRideDto.setStatus("ACCEPTED");

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto responseRideDto = objectMapper.readValue(result.getResponse().getContentAsString(), RideDto.class);

        prepareRideDtoBeforeAssertion(pendingRideDto);
        prepareRideDtoBeforeAssertion(responseRideDto);

        assertEquals(pendingRideDto, responseRideDto);

    }


    //  EndRide
    @Test
    public void endRide_shouldBeUnauthorized() throws Exception {
        int rideId = 1;
        String url = "/api/ride/" + rideId + "/end";
        String headerName = "Authorization";

        mockMvc.perform(put(url).header(headerName, "")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void endRide_shouldBeForbidden() throws Exception {
        int rideId = 1;
        String url = "/api/ride/" + rideId + "/end";
        String headerName = "Authorization";

        String token = "Bearer " + passenger1Token;
        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void endRide_shouldThrowRideDoesNotExist() throws Exception {

        int rideId = 123;
        String url = "/api/ride/" + rideId + "/end";
        String headerName = "Authorization";
        String token = "Bearer " + driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void endRide_shouldThrowRideCancellationExceptionWhenRideStatusIsNotActive() throws Exception {
        Ride ride = getFinishedRide();
        int rideId = Math.toIntExact(ride.getId());
        String url = "/api/ride/" + rideId + "/end";
        String headerName = "Authorization";
        String token = "Bearer " + driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isBadRequest()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals(errorDto.getMessage(), "Cannot end a ride that is not in status STARTED!");
    }

    @Test
    public void endRide_shouldEndRide() throws Exception {
        Ride activeRide = getActiveRide();
        int rideId = Math.toIntExact(activeRide.getId());

        String url = "/api/ride/" + rideId + "/end";
        String headerName = "Authorization";
        String token = "Bearer " + driverToken;
        RideDto activeRideDto = new RideDto(activeRide);

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto responseRideDto = objectMapper.readValue(result.getResponse().getContentAsString(), RideDto.class);

        activeRideDto.setEndTime(LocalDateTime.now().format(Constants.customDateTimeFormat));
        activeRideDto.setStatus("FINISHED");

        prepareRideDtoBeforeAssertion(activeRideDto);
        prepareRideDtoBeforeAssertion(responseRideDto);
        assertEquals(activeRideDto, responseRideDto);
    }


    // GetFavoriteLocations
    @Test
    public void getFavoriteLocations_shouldBeUnauthorized() throws Exception {

        String url = "/api/ride/favorites";
        String headerName = "Authorization";

        mockMvc.perform(get(url).header(headerName, "")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void getFavoriteLocations_shouldBeForbidden() throws Exception {
        String url = "/api/ride/favorites";
        String headerName = "Authorization";

        String token = "Bearer " + driverToken;
        mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void getFavouriteLocationById_shouldReturnFavouriteLocations() throws Exception {
        FavoriteLocation favoriteLocation = getFavoriteLocation();

        String url = "/api/ride/favorites";
        String headerName = "Authorization";
        String token = "Bearer " + passenger1Token;

        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(favoriteLocation);
        List<FavoriteLocationDto> favoriteLocationDtos = List.of(favoriteLocationDto);

        MvcResult result = mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        List<FavoriteLocationDto> responseFavoriteLocationDtos =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<FavoriteLocationDto>>() {
        });
        ;

        // TODO doadti pre poredjenja da se sortiraju lokacije i cela lista da se sortira po nekom atributu

        assertEquals(favoriteLocationDtos, responseFavoriteLocationDtos);

    }


    //    DeleteFavoriteRide
    @Test
    public void deleteFavouriteRide_shouldBeUnauthorized() throws Exception {
        int favoriteLocationId = 1;
        String url = "/api/ride/favorites/" + favoriteLocationId;
        String headerName = "Authorization";

        mockMvc.perform(delete(url).header(headerName, "")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void deleteFavouriteRide_shouldBeForbidden() throws Exception {
        int favoriteLocationId = 1;
        String url = "/api/ride/favorites/" + favoriteLocationId;
        String headerName = "Authorization";

        String token = "Bearer " + driverToken;
        mockMvc.perform(delete(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void deleteFavouriteRide_shouldThrowFavoriteLocationNotFound() throws Exception {

        int favoriteLocationId = 123;
        String url = "/api/ride/favorites/" + favoriteLocationId;
        String headerName = "Authorization";
        String token = "Bearer " + passenger1Token;

        MvcResult result = mockMvc.perform(delete(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Favorite location not found!", errorDto.getMessage());
    }

    @Test
    public void deleteFavouriteRide_shouldDelete() throws Exception {
        FavoriteLocation favoriteLocation = getFavoriteLocation();
        int favoriteLocationId = Math.toIntExact(favoriteLocation.getId());

        String url = "/api/ride/favorites/" + favoriteLocationId;
        String headerName = "Authorization";
        String token = "Bearer " + passenger1Token;


        MvcResult result = mockMvc.perform(delete(url).header(headerName, token)).andExpect(status().isNoContent()).andReturn();
        String responseMessage = result.getResponse().getContentAsString();
        assertEquals("Successful deletion of favorite location!", responseMessage);
    }


}
