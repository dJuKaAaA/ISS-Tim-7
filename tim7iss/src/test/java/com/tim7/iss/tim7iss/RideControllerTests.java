package com.tim7.iss.tim7iss;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tim7.iss.tim7iss.dto.ErrorDto;
import com.tim7.iss.tim7iss.dto.FavoriteLocationDto;
import com.tim7.iss.tim7iss.dto.RideDto;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverDocumentRequestRepository;
import com.tim7.iss.tim7iss.repositories.DriverRequestRepository;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Passenger passenger = customTestData.getPassenger1();
        customTestData.passenger1Token = login(passenger.getEmailAddress(), "Petar123");
        customTestData.driverToken = login(driver.getEmailAddress(), "Mika1234");
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

    /* Schedule ride tests:
    * 1. No auth
    * 2. Forbidden
    * 3. Happy flow
    * 4. Bad request all data is bad
    * 5. Bad date format
    * 6. No locations
    * 7. No passengers
    * 8. Null vehicle type
    * 9. Wrong vehicle type */

    /* Create favorite location tests:
    * 1. No auth
    * 2. Forbidden
    * 3. Happy flow
    * 4. Bad request all data is bad
    * 4. Empty favorite name
    * 5. No locations
    * 6. No passenger
    * 7. Null vehicle type
    * 8. Wrong vehicle type */

    /* Get active ride for driver tests:
    * 1. No auth
    * 2. Forbidden
    * 3. Happy flow
    * 4. User not found
    * 5. Ride not found */

}
