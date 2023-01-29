package com.tim7.iss.tim7iss;


import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.RideCancelationException;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.RideService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverService driverService;

    @Autowired
    @InjectMocks
    private RideService rideService;

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

        Ride ride = new Ride(null, 1000, LocalDateTime.now().minusMinutes(10), null,
                route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false,
                Enums.RideStatus.ACTIVE, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));
        return ride;
    }
    // Levo je ono sto ocekujemo
    // Desno je sta nam je vratola baza


    // getAllFinishedRides
    @Test
    public void getAllFinishedRides_shouldReturnFinishedRidesForPassengerId() throws IOException, UserNotFoundException {
        Passenger passenger = getPassenger();
        Ride ride = getFinishedRide();

        Mockito.when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Mockito.when(rideRepository.findRidesByPassengersId(passenger.getId())).thenReturn(List.of(ride));

        RideDto rideDto = new RideDto(ride);
        List<RideDto> rideDtos = List.of(rideDto);

        assertEquals(rideDtos, rideService.getAllFinishedRides(passenger.getId()));

    }

    @Test
    public void getAllFinishedRides_shouldReturnFinishedRidesForDriverId() throws IOException, UserNotFoundException {
        Driver driver = getDriver();
        Ride ride = getFinishedRide();

        Mockito.when(userRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findRidesByDriverId(driver.getId())).thenReturn(List.of(ride));

        RideDto rideDto = new RideDto(ride);
        List<RideDto> rideDtos = List.of(rideDto);


        assertEquals(rideService.getAllFinishedRides(driver.getId()), rideDtos);
    }

    @Test
    public void getAllFinishedRides_shouldReturnEmptyListWhenDriverOrPassengerNotHaveFinishedRides() throws IOException, UserNotFoundException {
        Passenger passenger = getPassenger();

        Mockito.when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Mockito.when(rideRepository.findRidesByPassengersId(passenger.getId())).thenReturn(new ArrayList<>());

        assertEquals(new ArrayList<>(), rideService.getAllFinishedRides(passenger.getId()));
    }

    @Test
    public void getAllFinishedRides_shouldThrowExceptionForUserNotFoundWhenUserIsNotDriverOrPassenger() throws IOException, UserNotFoundException {

        String errorMessage = "Only driver or passenger can have rides!";
        Admin admin = getAdmin();

        Mockito.when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            rideService.getAllFinishedRides(admin.getId());
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void getAllFinishedRides_shouldThrowUserNotFoundExceptionForNonExistingUser() throws UserNotFoundException {
        doAnswer(invocation -> {
            throw new UserNotFoundException();
        }).when(userRepository).findById(Mockito.anyLong());
        try {
            rideService.getAllFinishedRides(Mockito.anyLong());
        } catch (UserNotFoundException e) {
            assertEquals("User does not exist!", e.getMessage());
        }
    }


    // getAllRejectedRides
    @Test
    public void getAllRejectedRides_shouldReturnRejectedRidesForPassengerId() throws IOException, UserNotFoundException {
        Passenger passenger = getPassenger();
        Ride ride = getRejectedRide();

        Mockito.when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Mockito.when(rideRepository.findRidesByPassengersId(passenger.getId())).thenReturn(List.of(ride));

        RideDto rideDto = new RideDto(ride);
        List<RideDto> rideDtos = List.of(rideDto);

        assertEquals(rideService.getAllRejectedRides(passenger.getId()), rideDtos);
    }

    @Test
    public void getAllRejectedRides_forDriverId() throws UserNotFoundException, IOException {
        Driver driver = getDriver();
        Ride ride = getRejectedRide();

        Mockito.when(userRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findRidesByDriverId(driver.getId())).thenReturn(List.of(ride));

        RideDto rideDto = new RideDto(ride);
        List<RideDto> rideDtos = List.of(rideDto);


        assertEquals(rideDtos, rideService.getAllRejectedRides(driver.getId()));
    }

    @Test
    public void getAllRejectedRides_shouldThrowExceptionForUserNotFoundWhenUserIsNotDriverOrPassenger() throws IOException {
        Admin admin = getAdmin();

        Mockito.when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            rideService.getAllRejectedRides(admin.getId());
        });
        assertEquals("Only driver or passenger can have rides!", exception.getMessage());
    }

    @Test
    public void getAllRejectedRides_shouldThrowUserNotFoundExceptionForNonExistingUser() {
        doAnswer(invocation -> {
            throw new UserNotFoundException();
        }).when(userRepository).findById(Mockito.anyLong());
        try {
            rideService.getAllRejectedRides(Mockito.anyLong());
        } catch (UserNotFoundException e) {
            assertEquals("User does not exist!", e.getMessage());
        }
    }

    @Test
    public void getAllRejectedRides_shouldReturnEmptyListWhenDriverOrPassengerNotHaveRejectedRides() throws IOException, UserNotFoundException {
        Passenger passenger = getPassenger();

        Mockito.when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Mockito.when(rideRepository.findRidesByPassengersId(passenger.getId())).thenReturn(new ArrayList<>());

        assertEquals(new ArrayList<>(), rideService.getAllRejectedRides(passenger.getId()));
    }


    // getPaginatedRidesForDriverAsDto
    @Test
    public void getPaginatedRidesForDriverAsDto_shouldReturnPaginatedRides() throws IOException, DriverNotFoundException {
        Driver driver = getDriver();
        Ride ride = getFinishedRide();
        RideDto rideDto = new RideDto(ride);

        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findRidesByDriverId(Mockito.anyLong())).thenReturn(List.of(ride));


        PaginatedResponseDto expectedPaginatedResponseDto = new PaginatedResponseDto(List.of(rideDto).size(), List.of(rideDto));

        PaginatedResponseDto actualPaginatedResponseDto = rideService.getPaginatedRidesForDriverAsDto(Mockito.anyLong(), null);

        assertEquals(expectedPaginatedResponseDto, actualPaginatedResponseDto);
    }

    @Test
    public void getPaginatedRidesForDriverAsDto_shouldReturnPaginatedRidesWhenDriverHasNoRides() throws IOException, DriverNotFoundException {
        Driver driver = getDriver();


        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findRidesByDriverId(Mockito.anyLong())).thenReturn(new ArrayList<>());


        PaginatedResponseDto expectedPaginatedResponseDto = new PaginatedResponseDto(0, new ArrayList());

        PaginatedResponseDto actualPaginatedResponseDto = rideService.getPaginatedRidesForDriverAsDto(Mockito.anyLong(), null);

        assertEquals(expectedPaginatedResponseDto, actualPaginatedResponseDto);
    }

    @Test
    public void getPaginatedRidesForDriverAsDto_shouldThrowDriverNotFoundExceptionForNonExistingDriver() {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(driverRepository).findById(Mockito.anyLong());
        try {
            rideService.getPaginatedRidesForDriverAsDto(Mockito.anyLong(), null);
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }


    // getScheduledRidesForDriverAsDto
    @Test
    public void getScheduledRidesForDriverAsDto_shouldThrowDriverNotFoundFoundWhenIdOfDriverNotExist() {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(driverRepository).findById(Mockito.anyLong());
        try {
            rideService.getScheduledRidesForDriverAsDto(Mockito.anyLong());
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }

    @Test
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenOnlyHaveAcceptedRides() throws IOException, DriverNotFoundException {
        Driver driver = getDriver();
        Ride ride1 = getAcceptedRide();
        Ride ride2 = getAcceptedRide();
        ride2.setStartTime(ride2.getStartTime().plusMinutes(5));
        // empty
        List<Ride> pendingRides = new ArrayList<>();
        List<Ride> acceptedRides = List.of(ride1, ride2);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(pendingRides);
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(acceptedRides);

        Collection<Ride> rides = new ArrayList<>();
        rides.addAll(pendingRides);
        rides.addAll(acceptedRides);

        List<RideDto> expectedScheduledRidesDto = rides.stream().map(RideDto::new).toList();
        expectedScheduledRidesDto = expectedScheduledRidesDto.stream().sorted(Comparator.comparing((RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat))).toList();

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());

        assertEquals(expectedScheduledRidesDto, actualScheduledRides);

    }

    @Test
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenOnlyHavePendingRides() throws IOException, DriverNotFoundException {
        Driver driver = getDriver();
        Ride ride1 = getPendingRide();
        Ride ride2 = getPendingRide();
        ride2.setStartTime(ride2.getStartTime().plusMinutes(5));

        List<Ride> pendingRides = List.of(ride1, ride2);

        // empty
        List<Ride> acceptedRides = new ArrayList<>();

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(pendingRides);
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(acceptedRides);

        Collection<Ride> rides = new ArrayList<>();
        rides.addAll(pendingRides);
        rides.addAll(acceptedRides);

        List<RideDto> expectedScheduledRidesDto = rides.stream().map(RideDto::new).toList();
        expectedScheduledRidesDto = expectedScheduledRidesDto.stream().sorted(Comparator.comparing((RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat))).toList();

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());

        assertEquals(expectedScheduledRidesDto, actualScheduledRides);

    }

    @Test
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenHavePendingAndAcceptedRides() throws IOException, DriverNotFoundException {
        Driver driver = getDriver();
        Ride pendingRide1 = getPendingRide();
        Ride pendingRide2 = getPendingRide();
        pendingRide2.setStartTime(pendingRide2.getStartTime().plusMinutes(5));

        Ride acceptedRide1 = getAcceptedRide();
        Ride acceptedRide2 = getAcceptedRide();
        acceptedRide2.setStartTime(acceptedRide2.getStartTime().plusMinutes(8));

        List<Ride> pendingRides = List.of(pendingRide1, pendingRide2);
        List<Ride> acceptedRides = List.of(acceptedRide1, acceptedRide2);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(pendingRides);
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(acceptedRides);

        Collection<Ride> rides = new ArrayList<>();
        rides.addAll(pendingRides);
        rides.addAll(acceptedRides);

        List<RideDto> expectedScheduledRidesDto = rides.stream().map(RideDto::new).toList();
        expectedScheduledRidesDto = expectedScheduledRidesDto.stream().sorted(Comparator.comparing((RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat))).toList();

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());

        assertEquals(expectedScheduledRidesDto, actualScheduledRides);

    }

    @Test
    public void getScheduledRidesForDriverAsDto_shouldReturnEmptyListWhenDriverNotHaveScheduledRides() throws IOException, DriverNotFoundException {
        Driver driver = getDriver();

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(new ArrayList<>());

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());
        assertEquals(new ArrayList<>(), actualScheduledRides);
    }


    // setDriver
    @Test
    public void setDriver_shouldSetDriver() throws IOException, DriverNotFoundException, RideNotFoundException {
        Ride ride = getPendingRide();
        ride.setDriver(null);

        Driver driver = getDriver();
        Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ride));
        Mockito.when(driverService.findById(Mockito.anyLong())).thenReturn(driver);
        lenient().doNothing().when(driverService).save(driver);

        ride.setDriver(driver);
        RideDto rideDto = new RideDto(ride);

        RideAddDriverDto rideAddDriverDto = new RideAddDriverDto(1L, 1L);
        assertEquals(rideDto, rideService.setDriver(rideAddDriverDto));
    }

    @Test
    public void setDriver_shouldThrowRideNotFoundExceptionWhenIdOfRideNotFound() throws DriverNotFoundException {
        doAnswer(invocation -> {
            throw new RideNotFoundException();
        }).when(rideRepository).findById(Mockito.anyLong());
        try {
            RideAddDriverDto rideAddDriverDto = new RideAddDriverDto(1L, 1L);
            rideService.setDriver(rideAddDriverDto);
        } catch (RideNotFoundException e) {
            assertEquals("Ride does not exist!", e.getMessage());
        }
    }

    @Test
    public void setDriver_shouldThrowDriverNotFoundExceptionWhenIdOfDriverNotFound() throws IOException, RideNotFoundException {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(rideRepository).findById(Mockito.anyLong());
        try {
            Driver driver = getDriver();
            lenient().when(driverService.findById(Mockito.anyLong())).thenReturn(driver);
            RideAddDriverDto rideAddDriverDto = new RideAddDriverDto(1L, 1L);
            rideService.setDriver(rideAddDriverDto);
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }


    // startRide
    @Test
    public void startRide_shouldThrowRideNotFoundExceptionForNonExistingRideId() throws DriverNotFoundException, RideCancelationException {
        doAnswer(invocation -> {
            throw new RideNotFoundException();
        }).when(rideRepository).findById(Mockito.anyLong());
        try {
            rideService.startRide(1L, "");
        } catch (RideNotFoundException e) {
            assertEquals("Ride does not exist!", e.getMessage());
        }
    }

    @Test
    public void startRide_shouldThrowDriverNotFoundForNonExistingDriverId() throws RideCancelationException, IOException, RideNotFoundException {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(driverService).getByEmailAddress(Mockito.anyString());
        try {
            Ride acceptedRide = getAcceptedRide();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            rideService.startRide(1L, "");
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }

    @Test
    public void startRide_shouldThrowDriverNotFoundWhenDriverRideIdAndFoundDriverIdNotSame() throws RideCancelationException, IOException, DriverNotFoundException {
        try {

            Ride acceptedRide = getAcceptedRide();
            Driver driver = getDriver();
            driver.setId(123L);
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.startRide(1L, "");
        } catch (RideNotFoundException e) {
            assertEquals("Ride does not exist!", e.getMessage());
        }
    }

    @Test
    public void startRide_shouldThrowRideCancellationExceptionWhenRideStatusIsNotAccepted() throws IOException, DriverNotFoundException, RideNotFoundException {
        try {
            Ride rejectedRide = getRejectedRide();
            Driver driver = getDriver();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(rejectedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.startRide(1L, "");
        } catch (RideCancelationException e) {
            assertEquals("Cannot start a ride that is not in status ACCEPTED!", e.getMessage());
        }
    }

    @Test
    public void startRide_shouldStartRide() throws IOException, DriverNotFoundException, RideCancelationException, RideNotFoundException {
        Ride acceptedRide = getAcceptedRide();
        Driver driver = getDriver();
        Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
        Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
        lenient().doNothing().when(driverService).save(driver);
        RideDto actualRideDto = rideService.startRide(1L, "");

        acceptedRide.setStatus(Enums.RideStatus.ACTIVE);
        RideDto expectedRideDto = new RideDto(acceptedRide);
        assertEquals(expectedRideDto, actualRideDto);
    }


    // rejectRide

    @Test
    public void rejectRide_shouldThrowRideNotFoundExceptionForNonExistingRideId() throws DriverNotFoundException, RideCancelationException {
        doAnswer(invocation -> {
            throw new RideNotFoundException();
        }).when(rideRepository).findById(Mockito.anyLong());
        try {
            rideService.rejectRide(1L, "", null);
        } catch (RideNotFoundException e) {
            assertEquals("Ride does not exist!", e.getMessage());
        }
    }

    @Test
    public void rejectRide_shouldThrowDriverNotFoundForNonExistingDriverId() throws RideCancelationException, IOException, RideNotFoundException {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(driverService).getByEmailAddress(Mockito.anyString());
        try {
            Ride acceptedRide = getAcceptedRide();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            rideService.rejectRide(1L, "", null);
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }

    @Test
    public void rejectRide_shouldThrowDriverNotFoundWhenDriverRideIdAndFoundDriverIdNotSame() throws RideCancelationException, IOException, DriverNotFoundException {
        try {

            Ride acceptedRide = getAcceptedRide();
            Driver driver = getDriver();
            driver.setId(123L);
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.rejectRide(1L, "", null);
        } catch (RideNotFoundException e) {
            assertEquals("Ride does not exist!", e.getMessage());
        }
    }

    @Test
    public void rejectRide_shouldThrowRideCancellationExceptionWhenRideStatusIsNotAcceptedOrPadding() throws IOException, DriverNotFoundException, RideNotFoundException {
        try {
            Ride rejectedRide = getRejectedRide();
            Driver driver = getDriver();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(rejectedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.rejectRide(1L, "", null);
        } catch (RideCancelationException e) {
            assertEquals("Cannot cancel a ride that is not in status PENDING or ACCEPTED!", e.getMessage());
        }
    }

    @Test
    public void rejectRide_shouldRejectAcceptedRide() throws IOException, DriverNotFoundException, RideCancelationException, RideNotFoundException {
        Ride acceptedRide = getAcceptedRide();
        Driver driver = getDriver();
        Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
        Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
        lenient().doNothing().when(driverService).save(driver);

        PanicCreateDto panicCreateDto = new PanicCreateDto("Neka random poruka!");
        RideDto actualRideDto = rideService.rejectRide(1L, "", panicCreateDto);

        acceptedRide.setStatus(Enums.RideStatus.CANCELED);
        RideDto expectedRideDto = new RideDto(acceptedRide);
        assertEquals(expectedRideDto, actualRideDto);
    }


    @Test
    public void rejectRide_shouldRejectPendingRide() throws IOException, DriverNotFoundException,
            RideCancelationException,
            RideNotFoundException {
        Ride pendingRide = getPendingRide();
        Driver driver = getDriver();
        Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pendingRide));
        Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
        lenient().doNothing().when(driverService).save(driver);

        PanicCreateDto panicCreateDto = new PanicCreateDto("Neka random poruka!");
        RideDto actualRideDto = rideService.rejectRide(1L, "", panicCreateDto);

        pendingRide.setStatus(Enums.RideStatus.CANCELED);
        RideDto expectedRideDto = new RideDto(pendingRide);
        assertEquals(expectedRideDto, actualRideDto);
    }


    // endRide
    @Test
    public void endRide_shouldThrowRideNotFoundExceptionForNonExistingRideId() throws DriverNotFoundException,
            RideCancelationException {
        doAnswer(invocation -> {
            throw new RideNotFoundException();
        }).when(rideRepository).findById(Mockito.anyLong());
        try {
            rideService.endRide(1L, "");
        } catch (RideNotFoundException e) {
            assertEquals("Ride does not exist!", e.getMessage());
        }
    }

    @Test
    public void endRide_shouldThrowDriverNotFoundForNonExistingDriverId() throws RideCancelationException, IOException,
            RideNotFoundException {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(driverService).getByEmailAddress(Mockito.anyString());
        try {
            Ride acceptedRide = getAcceptedRide();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            rideService.endRide(1L, "");
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }

    @Test
    public void endRide_shouldThrowDriverNotFoundWhenDriverRideIdAndFoundDriverIdNotSame() throws RideCancelationException,
            IOException, DriverNotFoundException {
        try {

            Ride acceptedRide = getAcceptedRide();
            Driver driver = getDriver();
            driver.setId(123L);
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.endRide(1L, "");
        } catch (RideNotFoundException e) {
            assertEquals("Ride does not exist!", e.getMessage());
        }
    }

    @Test
    public void endRide_shouldThrowRideCancellationExceptionWhenRideStatusIsNotAActive() throws IOException,
            DriverNotFoundException, RideNotFoundException {
        try {
            Ride rejectedRide = getRejectedRide();
            Driver driver = getDriver();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(rejectedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.endRide(1L, "");
        } catch (RideCancelationException e) {
            assertEquals("Cannot end a ride that is not in status STARTED!", e.getMessage());
        }
    }

    @Test
    public void endRide_shouldEndRide() throws IOException, DriverNotFoundException,
            RideCancelationException,
            RideNotFoundException {
        Ride activeRide = getActiveRide();
        Driver driver = getDriver();
        Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(activeRide));
        Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
        lenient().doNothing().when(driverService).save(driver);


        RideDto actualRideDto = rideService.endRide(1L, "");

        activeRide.setStatus(Enums.RideStatus.FINISHED);
        activeRide.setEndTime(LocalDateTime.now());
        RideDto expectedRideDto = new RideDto(activeRide);
        assertEquals(expectedRideDto, actualRideDto);
    }


}
