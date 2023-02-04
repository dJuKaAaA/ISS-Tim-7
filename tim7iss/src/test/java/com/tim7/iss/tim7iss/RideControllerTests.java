package com.tim7.iss.tim7iss;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverDocumentRequestRepository;
import com.tim7.iss.tim7iss.repositories.DriverRequestRepository;
import com.tim7.iss.tim7iss.repositories.PassengerRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideControllerTests {


    public CustomTestData customTestData = new CustomTestData();

    @Autowired
    DriverDocumentRequestRepository driverDocumentRequestRepository;

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private RideRepository rideRepository;

    @Autowired
    DriverRequestRepository driverRequestRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private MockMvc mockMvc;

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


    @BeforeAll
    void beforeAll() throws IOException {
        Admin admin = customTestData.getAdmin();
        Driver driver = customTestData.getDriver();
        Driver driver2 = customTestData.getDriver2();
        Passenger passenger = customTestData.getPassenger1();
        customTestData.passenger1Token = login(passenger.getEmailAddress(), "Petar123");
        customTestData.driverToken = login(driver.getEmailAddress(), "Mika1234");
        customTestData.driver2Token = login(driver2.getEmailAddress(), "Mika1234");
        customTestData.adminToken = login(admin.getEmailAddress(), "Admin123");
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
        String token = "Bearer " + customTestData.driverToken;

        MvcResult result = mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void getRideById_shouldReturnRide() throws Exception {
        Ride finishedRide = customTestData.getFinishedRide();
        int finishedRideId = Math.toIntExact(finishedRide.getId());
        String url = "/api/ride/" + finishedRideId;
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        RideDto finishedRideDto = new RideDto(finishedRide);
        MvcResult result = mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto responseRideDto = objectMapper.readValue(result.getResponse().getContentAsString(), RideDto.class);

        customTestData.prepareRideDtoBeforeAssertion(finishedRideDto);
        customTestData.prepareRideDtoBeforeAssertion(responseRideDto);

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
        String token = "Bearer " + customTestData.passenger1Token;
        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();

    }

    @Test
    public void acceptRide_shouldThrowRideDoesNotExistWhenRideIdNotExist() throws Exception {
        int rideId = 123;
        String url = "/api/ride/" + rideId + "/accept";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void acceptRide_shouldThrowRideCancellationExceptionWhenRideStatusIsPending() throws Exception {
        Ride ride = customTestData.getPendingRide();
        int rideId = Math.toIntExact(customTestData.getPendingRide().getId());
        String url = "/api/ride/" + rideId + "/accept";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isBadRequest()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Cannot accept a ride that is not in status PENDING!", errorDto.getMessage());
    }

    @Test
    public void acceptRide_shouldAcceptRide() throws Exception {
        Ride pendingRide = customTestData.getPendingRide();

        int pendingRideId = Math.toIntExact(pendingRide.getId());
        String url = "/api/ride/" + pendingRideId + "/accept";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        RideDto pendingRideDto = new RideDto(pendingRide);
        pendingRideDto.setStatus("ACCEPTED");

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto responseRideDto = objectMapper.readValue(result.getResponse().getContentAsString(), RideDto.class);

        customTestData.prepareRideDtoBeforeAssertion(pendingRideDto);
        customTestData.prepareRideDtoBeforeAssertion(responseRideDto);

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

        String token = "Bearer " + customTestData.passenger1Token;
        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void endRide_shouldThrowRideDoesNotExist() throws Exception {

        int rideId = 123;
        String url = "/api/ride/" + rideId + "/end";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void endRide_shouldThrowRideCancellationExceptionWhenRideStatusIsNotActive() throws Exception {
        Ride ride = customTestData.getFinishedRide();
        int rideId = Math.toIntExact(ride.getId());
        String url = "/api/ride/" + rideId + "/end";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isBadRequest()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals(errorDto.getMessage(), "Cannot end a ride that is not in status STARTED!");
    }

    @Test
    public void endRide_shouldEndRide() throws Exception {
        Ride activeRide = customTestData.getActiveRide();
        int rideId = Math.toIntExact(activeRide.getId());

        String url = "/api/ride/" + rideId + "/end";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;
        RideDto activeRideDto = new RideDto(activeRide);

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto responseRideDto = objectMapper.readValue(result.getResponse().getContentAsString(), RideDto.class);

        activeRideDto.setEndTime(LocalDateTime.now().format(Constants.customDateTimeFormat));
        activeRideDto.setStatus("FINISHED");

        customTestData.prepareRideDtoBeforeAssertion(activeRideDto);
        customTestData.prepareRideDtoBeforeAssertion(responseRideDto);
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

        String token = "Bearer " + customTestData.driverToken;
        mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void getFavouriteLocationById_shouldReturnFavouriteLocations() throws Exception {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();

        String url = "/api/ride/favorites";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;

        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(favoriteLocation);
        List<FavoriteLocationDto> favoriteLocationDtos = List.of(favoriteLocationDto);

        MvcResult result = mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        List<FavoriteLocationDto> responseFavoriteLocationDtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<FavoriteLocationDto>>() {
        });

        // TODO doadti pre poredjenja da se sortiraju lokacije i cela lista da se sortira po nekom atributu

        assertEquals(favoriteLocationDtos, responseFavoriteLocationDtos);

    }


    //  DeleteFavoriteRide
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

        String token = "Bearer " + customTestData.driverToken;
        mockMvc.perform(delete(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void deleteFavouriteRide_shouldThrowFavoriteLocationNotFound() throws Exception {

        int favoriteLocationId = 123;
        String url = "/api/ride/favorites/" + favoriteLocationId;
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;

        MvcResult result = mockMvc.perform(delete(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Favorite location not found!", errorDto.getMessage());
    }

    @Test
    public void deleteFavouriteRide_shouldDelete() throws Exception {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        int favoriteLocationId = Math.toIntExact(favoriteLocation.getId());

        String url = "/api/ride/favorites/" + favoriteLocationId;
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;


        MvcResult result = mockMvc.perform(delete(url).header(headerName, token)).andExpect(status().isNoContent()).andReturn();
        String responseMessage = result.getResponse().getContentAsString();
        assertEquals("Successful deletion of favorite location!", responseMessage);
    }

    @Test
    public void scheduleRide_ShouldThrow401Error() throws Exception {
        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)).andExpect(status().isUnauthorized());
    }

    @Test
    public void scheduleRide_ShouldThrow403Error() throws Exception {
        LocationForRideDto location = new LocationForRideDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f),
                8000,
                10
        );
        Passenger passenger = customTestData.getPassenger1();
        RideCreationDto rideCreationDto = new RideCreationDto(
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat),
                List.of(location),
                List.of(new UserRefDto(passenger)),
                "STANDARD",
                false,
                false
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rideCreationDto);

        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + customTestData.driverToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isForbidden());
    }

    @Test
    public void scheduleRide_ShouldSuccessfullyScheduleRide() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        LocationForRideDto location = new LocationForRideDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f),
                8000,
                10
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        RideCreationDto rideCreationDto = new RideCreationDto(
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat),
                List.of(location),
                List.of(new UserRefDto(passenger)),
                "STANDARD",
                false,
                false
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rideCreationDto);

        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isOk());
    }

    @Test
    public void scheduleRide_ShouldThrowBadRequestIfEntireRequestBodyIsInvalid() throws Exception {
        RideCreationDto rideCreationDto = new RideCreationDto(
                null,
                null,
                null,
                null,
                null,
                null
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rideCreationDto);

        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + customTestData.driverToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isBadRequest());
    }

    @Test
    public void scheduleRide_ShouldThrowBadRequestIfDateFormatIsBad() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        LocationForRideDto location = new LocationForRideDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f),
                8000,
                10
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        RideCreationDto rideCreationDto = new RideCreationDto(
                "dsijnodfgsijodfgshiodfgs",
                List.of(location),
                List.of(new UserRefDto(passenger)),
                "STANDARD",
                false,
                false
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rideCreationDto);

        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isBadRequest());
    }

    @Test
    public void scheduleRide_ShouldThrowBadRequestIfLocationsIsEmpty() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        Passenger passenger = passengerRepository.findById(7L).get();
        RideCreationDto rideCreationDto = new RideCreationDto(
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat),
                new ArrayList<>(),
                List.of(new UserRefDto(passenger)),
                "STANDARD",
                false,
                false
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rideCreationDto);

        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isBadRequest());
    }

    @Test
    public void scheduleRide_ShouldThrowBadRequestIfPassengersIsEmpty() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        LocationForRideDto location = new LocationForRideDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f),
                8000,
                10
        );
        RideCreationDto rideCreationDto = new RideCreationDto(
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat),
                List.of(location),
                new ArrayList<>(),
                "STANDARD",
                false,
                false
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rideCreationDto);

        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isBadRequest());
    }

    @Test
    public void scheduleRide_ShouldThrowBadRequestIfVehicleTypeIsNull() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        LocationForRideDto location = new LocationForRideDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f),
                8000,
                10
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        RideCreationDto rideCreationDto = new RideCreationDto(
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat),
                List.of(location),
                List.of(new UserRefDto(passenger)),
                null,
                false,
                false
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rideCreationDto);

        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isBadRequest());
    }

    @Test
    public void scheduleRide_ShouldThrowBadRequestIfVehicleTypeIsNotRecognized() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        LocationForRideDto location = new LocationForRideDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f),
                8000,
                10
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        RideCreationDto rideCreationDto = new RideCreationDto(
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat),
                List.of(location),
                List.of(new UserRefDto(passenger)),
                "ULTRA_MEGA_SUPER_DUPER",
                false,
                false
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rideCreationDto);

        String pathUrl = "/api/ride";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isBadRequest());
    }

    @Test
    public void createFavoriteLocation_ShouldThrow401Error() throws Exception {
        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)).andExpect(status().isUnauthorized());
    }

    @Test
    public void createFavoriteLocation_ShouldThrow403Error() throws Exception {
        RouteDto route = new RouteDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f)
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(
                null,
                "Home to University",
                Set.of(route),
                Set.of(new UserRefDto(passenger)),
                "STANDARD",
                false,
                false,
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat)
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(favoriteLocationDto);

        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)
                .header("Authorization", "Bearer " + customTestData.driverToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createFavoriteLocation_ShouldCreateFavoriteLocation() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        RouteDto route = new RouteDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f)
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(
                null,
                "Home to University",
                Set.of(route),
                Set.of(new UserRefDto(passenger)),
                "STANDARD",
                false,
                false,
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat)
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(favoriteLocationDto);

        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void createFavoriteLocation_ShouldThrowBadRequestIfRequestBodyIsInvalid() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                null
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(favoriteLocationDto);

        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFavoriteLocation_ShouldThrowBadRequestIfFavoriteNameIsEmpty() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        RouteDto route = new RouteDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f)
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(
                null,
                "",
                Set.of(route),
                Set.of(new UserRefDto(passenger)),
                "STANDARD",
                false,
                false,
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat)
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(favoriteLocationDto);

        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFavoriteLocation_ShouldThrowBadRequestIfLocationsAreEmpty() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        Passenger passenger = passengerRepository.findById(7L).get();
        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(
                null,
                "Home to University",
                new HashSet<>(),
                Set.of(new UserRefDto(passenger)),
                "STANDARD",
                false,
                false,
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat)
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(favoriteLocationDto);

        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFavoriteLocation_ShouldThrowBadRequestIfPassengersAreEmpty() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        RouteDto route = new RouteDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f)
        );
        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(
                null,
                "Home to University",
                Set.of(route),
                new HashSet<>(),
                "STANDARD",
                false,
                false,
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat)
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(favoriteLocationDto);

        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFavoriteLocation_ShouldThrowBadRequestIfVehicleTypeIsNull() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        RouteDto route = new RouteDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f)
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(
                null,
                "Home to University",
                Set.of(route),
                Set.of(new UserRefDto(passenger)),
                null,
                false,
                false,
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat)
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(favoriteLocationDto);

        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFavoriteLocation_ShouldThrowBadRequestIfVehicleTypeIsNotRecognized() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        RouteDto route = new RouteDto(
                new GeoCoordinateDto("", 45f, 20f),
                new GeoCoordinateDto("", 43f, 21f)
        );
        Passenger passenger = passengerRepository.findById(7L).get();
        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(
                null,
                "Home to University",
                Set.of(route),
                Set.of(new UserRefDto(passenger)),
                "ULTRA_MEGA_SUPER_DUPER",
                false,
                false,
                LocalDateTime.now().plusMinutes(6).format(Constants.customDateTimeFormat)
        );

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(favoriteLocationDto);

        String pathUrl = "/api/ride/favorites";
        mockMvc.perform(post(pathUrl)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPassengersActiveRide_ShouldThrow401Error() throws Exception {
        String urlPath = "/api/ride/passenger/7/active";
        mockMvc.perform(get(urlPath)).andExpect(status().isUnauthorized());
    }

    @Test
    public void getPassengersActiveRide_ShouldThrow403Error() throws Exception {
        String urlPath = "/api/ride/passenger/7/active";
        mockMvc.perform(get(urlPath).header("Authorization", "Bearer " + customTestData.driverToken)).andExpect(status().isForbidden());
    }

    @Test
    public void getPassengersActiveRide_ShouldThrowUserNotFoundExceptionIfUserDoesNotExist() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        Long nonExistentId = 23456789234568245L;
        String urlPath = "/api/ride/passenger/" + nonExistentId + "/active";
        mockMvc.perform(get(urlPath).header("Authorization", "Bearer " + jwt)).andExpect(status().isNotFound());
    }


    @Test
    public void getPassengersActiveRide_ShouldThrowRideNotFoundExceptionIfPassengerHasNoActiveRides() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        String urlPath = "/api/ride/passenger/7/active";
        mockMvc.perform(get(urlPath).header("Authorization", "Bearer " + jwt)).andExpect(status().isNotFound());
    }

    @Test
    public void getPassengersActiveRide_ShouldFetchActiveRideForPassenger() throws Exception {
        String jwt = login("neko.nekic@email.com", "Jovan123");

        Passenger passenger = passengerRepository.findById(7L).get();
        rideRepository.save(new Ride(null, 1000, LocalDateTime.now(), null, 10, false, false, false, Enums.RideStatus.ACTIVE, customTestData.getDriver(), customTestData.getVehicleType(), Set.of(passenger), null, null));
        String urlPath = "/api/ride/passenger/" + passenger.getId() + "/active";
        mockMvc.perform(get(urlPath).header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
    }

    public void startRide_forbiden() throws Exception {
        Ride ride = customTestData.getRideForAcceptance();
        String url = "/api/ride/"+ ride.getId() + "/start";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;

        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void startRide_unauthorized() throws Exception {
        Ride ride = customTestData.getRideForAcceptance();
        String url = "/api/ride/"+ ride.getId() + "/start";
        String headerName = "Authorization";
        String token = "Bearer ";

        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void startRide_rideNotFound() throws Exception {
        String url = "/api/ride/"+ 20 + "/start";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void startRide_rideIsNotAccepted() throws Exception {
        Ride ride = customTestData.getRideForAcceptance();
        String url = "/api/ride/"+ ride.getId() + "/start";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isBadRequest()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Cannot start a ride that is not in status ACCEPTED!", errorDto.getMessage());
    }

    @Test
    public void startRide_happyCase() throws Exception {
        Ride ride = customTestData.getRideForAcceptance();
        String url = "/api/ride/"+ ride.getId() + "/start";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto startedRide = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RideDto>() {});
        assertEquals("ACTIVE", startedRide.getStatus());
    }

    @Test
    public void cancelRide_forbidden() throws Exception {
        Ride ride = customTestData.getRideForCancelation();
        String url = "/api/ride/"+ ride.getId() + "/cancel";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;
        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        mockMvc.perform(put(url).header(headerName, token).contentType(APPLICATION_JSON_UTF8).content(panicCreateDtoJson)).andExpect(status().isForbidden()).andReturn();
    }
    @Test
    public void cancelRide_unauthorized() throws Exception {
        Ride ride = customTestData.getRideForCancelation();
        String url = "/api/ride/"+ ride.getId() + "/cancel";
        String headerName = "Authorization";
        String token = "Bearer ";
        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        mockMvc.perform(put(url).header(headerName, token).contentType(APPLICATION_JSON_UTF8).content(panicCreateDtoJson)).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void cancelRide_noRequestBody() throws Exception {
        Ride ride = customTestData.getRideForCancelation();
        String url = "/api/ride/"+ ride.getId() + "/cancel";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;;

        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void cancelRide_rideNotFound() throws Exception {
        String url = "/api/ride/"+ 20 + "/cancel";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;
        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        MvcResult result = mockMvc.perform(put(url).header(headerName, token).contentType(APPLICATION_JSON_UTF8).content(panicCreateDtoJson)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void cancelRide_rideIsNotPendingOrAccepted() throws Exception {
        Ride ride = customTestData.getFinishedRide();
        String url = "/api/ride/"+ ride.getId() + "/cancel";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;
        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        MvcResult result = mockMvc.perform(put(url).header(headerName, token).contentType(APPLICATION_JSON_UTF8).content(panicCreateDtoJson)).andExpect(status().isBadRequest()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Cannot cancel a ride that is not in status PENDING or ACCEPTED!", errorDto.getMessage());
    }

    @Test
    public void cancelRide_happyCase() throws Exception {
        Ride ride = customTestData.getRideForCancelation();
        String url = "/api/ride/"+ ride.getId() + "/cancel";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;
        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        MvcResult result = mockMvc.perform(put(url).header(headerName, token).content(panicCreateDtoJson).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto startedRide = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RideDto>() {});
        assertEquals("CANCELED", startedRide.getStatus());
    }

    @Test
    public void creatingPanicProcedure_unathorized() throws Exception {
        Ride ride = customTestData.getRideForWithdrawal();
        String url = "/api/ride/"+ ride.getId() + "/panic";
        String headerName = "Authorization";
        String token = "Bearer ";
        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        mockMvc.perform(put(url).header(headerName, token).contentType(APPLICATION_JSON_UTF8).content(panicCreateDtoJson)).andExpect(status().isUnauthorized()).andReturn();

    }

    @Test
    public void creatingPanicProcedure_forbidden() throws Exception {
        Ride ride = customTestData.getRideForWithdrawal();
        String url = "/api/ride/"+ ride.getId() + "/panic";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.adminToken;
        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        mockMvc.perform(put(url).header(headerName, token).contentType(APPLICATION_JSON_UTF8).content(panicCreateDtoJson)).andExpect(status().isForbidden()).andReturn();

    }

    @Test
    public void creatingPanicProcedure_rideNotFound() throws Exception {
        String url = "/api/ride/"+ 20 + "/panic";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;
        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        MvcResult result = mockMvc.perform(put(url).header(headerName, token).contentType(APPLICATION_JSON_UTF8).content(panicCreateDtoJson)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void creatingPanicProcedure_driverHappyCase() throws Exception {
        Ride ride = customTestData.getRideForWithdrawal();
        Driver driver = customTestData.getDriver();

        String url = "/api/ride/"+ ride.getId() + "/panic";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driverToken;

        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        PanicDetailsDto panicDetailsDto = new PanicDetailsDto(null, new UserDto(driver), new RideDto(ride), null, panicCreateDto.getReason(), false);

        MvcResult result = mockMvc.perform(put(url).header(headerName, token).content(panicCreateDtoJson).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        PanicDetailsDto panicDetailsDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PanicDetailsDto>() {});
        panicDetailsDto.setId(panicDetailsDtoResponse.getId());
        panicDetailsDto.setTime(panicDetailsDtoResponse.getTime());
        assertEquals(panicDetailsDto, panicDetailsDtoResponse);
    }

    @Test
    public void creatingPanicProcedure_passengerHappyCase() throws Exception {
        Ride ride = customTestData.getFinishedRide();
        Passenger passenger = customTestData.getPassenger1();

        String url = "/api/ride/"+ ride.getId() + "/panic";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;

        PanicCreateDto panicCreateDto = new PanicCreateDto();
        panicCreateDto.setReason("Passengers haven't arrived");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String panicCreateDtoJson = ow.writeValueAsString(panicCreateDto);

        PanicDetailsDto panicDetailsDto = new PanicDetailsDto(null, new UserDto(passenger), new RideDto(ride), null, panicCreateDto.getReason(), false);

        MvcResult result = mockMvc.perform(put(url).header(headerName, token).content(panicCreateDtoJson).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        PanicDetailsDto panicDetailsDtoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PanicDetailsDto>() {});
        panicDetailsDto.setId(panicDetailsDtoResponse.getId());
        panicDetailsDto.setTime(panicDetailsDtoResponse.getTime());
        assertEquals(panicDetailsDto, panicDetailsDtoResponse);
    }

    @Test
    public void withdrawRide_forbidden() throws Exception {
        Ride ride = customTestData.getRideForWithdrawal();
        String url = "/api/ride/"+ ride.getId() + "/withdraw";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driver2Token;

        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void withdrawRide_unauthorized() throws Exception {
        Ride ride = customTestData.getRideForWithdrawal();
        String url = "/api/ride/"+ ride.getId() + "/withdraw";
        String headerName = "Authorization";
        String token = "Bearer ";

        mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void withdrawRide_rideNotFound() throws Exception {
        String url = "/api/ride/"+ 20 + "/withdraw";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Ride does not exist!", errorDto.getMessage());
    }

    @Test
    public void withdrawRide_rideIsNotPendingOrActive() throws Exception {
        Ride ride = customTestData.getFinishedRide();
        String url = "/api/ride/"+ ride.getId() + "/withdraw";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isBadRequest()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertEquals("Cannot cancel a ride that is not in status PENDING or STARTED!", errorDto.getMessage());
    }

    @Test
    public void withdrawRide_happyCase() throws Exception {
        Ride ride = customTestData.getRideForWithdrawal();
        String url = "/api/ride/"+ ride.getId() + "/withdraw";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.passenger1Token;

        MvcResult result = mockMvc.perform(put(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        RideDto startedRide = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RideDto>() {});
        assertEquals("REJECTED", startedRide.getStatus());
    }

    @Test
    public void getDriversActiveRide_unauthorized() throws Exception {
        Driver driver = customTestData.getDriver2();
        String url = "/api/ride/driver/"+ driver.getId() + "/active";
        String headerName = "Authorization";
        String token = "Bearer ";

        mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void getDriversActiveRide_driverNotFound() throws Exception {
        String url = "/api/ride/driver/20/active";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driver2Token;

        mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void getDriversActiveRide_happyCase() throws Exception {
        Ride activeRide = customTestData.getDriver2ActiveRide();
        Driver driver = customTestData.getDriver2();
        String url = "/api/ride/driver/"+ driver.getId() + "/active";
        String headerName = "Authorization";
        String token = "Bearer " + customTestData.driver2Token;
        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(get(url).header(headerName, token)).andExpect(status().isOk()).andReturn();

        RideDto activeRideResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<RideDto>() {});
        assertEquals(new RideDto(activeRide), activeRideResponse);
    }

}
