package com.tim7.iss.tim7iss;


import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import com.tim7.iss.tim7iss.services.*;
import org.h2.engine.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceTests {

    public CustomTestData customTestData = new CustomTestData();
    @Mock
    private UserRepository userRepository;
    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private PassengerService passengerService;

    @Mock
    private FavoriteLocationService favoriteLocationService;

    @Mock
    private VehicleTypeService vehicleTypeService;

    @Mock
    private UserService userService;

    @Mock
    private PanicService panicService;

    @Autowired
    @InjectMocks
    private RideService rideService;

    // getAllFinishedRides
    @Test
    public void getAllFinishedRides_shouldReturnFinishedRidesForPassengerId() throws IOException, UserNotFoundException {
        Passenger passenger = customTestData.getPassenger1();
        Ride ride = customTestData.getFinishedRide();

        Mockito.when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Mockito.when(rideRepository.findRidesByPassengersId(passenger.getId())).thenReturn(List.of(ride));

        RideDto rideDto = new RideDto(ride);
        List<RideDto> rideDtos = List.of(rideDto);

        assertEquals(rideDtos, rideService.getAllFinishedRides(passenger.getId()));

    }

    @Test
    public void getAllFinishedRides_shouldReturnFinishedRidesForDriverId() throws IOException, UserNotFoundException {
        Driver driver = customTestData.getDriver();
        Ride ride = customTestData.getFinishedRide();

        Mockito.when(userRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findRidesByDriverId(driver.getId())).thenReturn(List.of(ride));

        RideDto rideDto = new RideDto(ride);
        List<RideDto> rideDtos = List.of(rideDto);


        assertEquals(rideService.getAllFinishedRides(driver.getId()), rideDtos);
    }

    @Test
    public void getAllFinishedRides_shouldReturnEmptyListWhenDriverOrPassengerNotHaveFinishedRides() throws IOException, UserNotFoundException {
        Passenger passenger = customTestData.getPassenger1();

        Mockito.when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Mockito.when(rideRepository.findRidesByPassengersId(passenger.getId())).thenReturn(new ArrayList<>());

        assertEquals(new ArrayList<>(), rideService.getAllFinishedRides(passenger.getId()));
    }

    @Test
    public void getAllFinishedRides_shouldThrowExceptionForUserNotFoundWhenUserIsNotDriverOrPassenger() throws IOException, UserNotFoundException {

        String errorMessage = "Only driver or passenger can have rides!";
        Admin admin = customTestData.getAdmin();

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
        Passenger passenger = customTestData.getPassenger1();
        Ride ride = customTestData.getRejectedRide();

        Mockito.when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Mockito.when(rideRepository.findRidesByPassengersId(passenger.getId())).thenReturn(List.of(ride));

        RideDto rideDto = new RideDto(ride);
        List<RideDto> rideDtos = List.of(rideDto);

        assertEquals(rideService.getAllRejectedRides(passenger.getId()), rideDtos);
    }

    @Test
    public void getAllRejectedRides_forDriverId() throws UserNotFoundException, IOException {
        Driver driver = customTestData.getDriver();
        Ride ride = customTestData.getRejectedRide();

        Mockito.when(userRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findRidesByDriverId(driver.getId())).thenReturn(List.of(ride));

        RideDto rideDto = new RideDto(ride);
        List<RideDto> rideDtos = List.of(rideDto);


        assertEquals(rideDtos, rideService.getAllRejectedRides(driver.getId()));
    }

    @Test
    public void getAllRejectedRides_shouldThrowExceptionForUserNotFoundWhenUserIsNotDriverOrPassenger() throws IOException {
        Admin admin = customTestData.getAdmin();

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
        Passenger passenger = customTestData.getPassenger1();

        Mockito.when(userRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Mockito.when(rideRepository.findRidesByPassengersId(passenger.getId())).thenReturn(new ArrayList<>());

        assertEquals(new ArrayList<>(), rideService.getAllRejectedRides(passenger.getId()));
    }


    // getPaginatedRidesForDriverAsDto
    @Test
    public void getPaginatedRidesForDriverAsDto_shouldReturnPaginatedRides() throws IOException, DriverNotFoundException {
        Driver driver = customTestData.getDriver();
        Ride ride = customTestData.getFinishedRide();
        RideDto rideDto = new RideDto(ride);

        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findRidesByDriverId(Mockito.anyLong())).thenReturn(List.of(ride));


        PaginatedResponseDto expectedPaginatedResponseDto = new PaginatedResponseDto(List.of(rideDto).size(), List.of(rideDto));

        PaginatedResponseDto actualPaginatedResponseDto = rideService.getPaginatedRidesForDriverAsDto(Mockito.anyLong(), null);

        assertEquals(expectedPaginatedResponseDto, actualPaginatedResponseDto);
    }

    @Test
    public void getPaginatedRidesForDriverAsDto_shouldReturnPaginatedRidesWhenDriverHasNoRides() throws IOException, DriverNotFoundException {
        Driver driver = customTestData.getDriver();


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
    public void getScheduledRidesForDriverAsDto_shouldThrowDriverNotFoundFoundWhenIdOfDriverNotExist() throws RideNotFoundException {
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
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenOnlyHaveAcceptedRides() throws IOException, DriverNotFoundException, RideNotFoundException {
        Driver driver = customTestData.getDriver();
        Ride ride1 = customTestData.getAcceptedRide();
        Ride ride2 = customTestData.getAcceptedRide();
        ride2.setStartTime(ride2.getStartTime().plusMinutes(5));
        // empty
        List<Ride> pendingRides = new ArrayList<>();
        List<Ride> acceptedRides = List.of(ride1, ride2);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(Optional.of(pendingRides));
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(Optional.of(acceptedRides));

        Collection<Ride> rides = new ArrayList<>();
        rides.addAll(pendingRides);
        rides.addAll(acceptedRides);

        List<RideDto> expectedScheduledRidesDto = rides.stream().map(RideDto::new).toList();
        expectedScheduledRidesDto = expectedScheduledRidesDto.stream().sorted(Comparator.comparing((RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat))).toList();

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());

        assertEquals(expectedScheduledRidesDto, actualScheduledRides);

    }

    @Test
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenOnlyHavePendingRides() throws IOException, DriverNotFoundException, RideNotFoundException {
        Driver driver = customTestData.getDriver();
        Ride ride1 = customTestData.getPendingRide();
        Ride ride2 = customTestData.getPendingRide();
        ride2.setStartTime(ride2.getStartTime().plusMinutes(5));

        List<Ride> pendingRides = List.of(ride1, ride2);

        // empty
        List<Ride> acceptedRides = new ArrayList<>();

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(Optional.of(pendingRides));
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(Optional.of(acceptedRides));

        Collection<Ride> rides = new ArrayList<>();
        rides.addAll(pendingRides);
        rides.addAll(acceptedRides);

        List<RideDto> expectedScheduledRidesDto = rides.stream().map(RideDto::new).toList();
        expectedScheduledRidesDto = expectedScheduledRidesDto.stream().sorted(Comparator.comparing((RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat))).toList();

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());

        assertEquals(expectedScheduledRidesDto, actualScheduledRides);

    }

    @Test
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenHavePendingAndAcceptedRides() throws IOException, DriverNotFoundException, RideNotFoundException {
        Driver driver = customTestData.getDriver();
        Ride pendingRide1 = customTestData.getPendingRide();
        Ride pendingRide2 = customTestData.getPendingRide();
        pendingRide2.setStartTime(pendingRide2.getStartTime().plusMinutes(5));

        Ride acceptedRide1 = customTestData.getAcceptedRide();
        Ride acceptedRide2 = customTestData.getAcceptedRide();
        acceptedRide2.setStartTime(acceptedRide2.getStartTime().plusMinutes(8));

        List<Ride> pendingRides = List.of(pendingRide1, pendingRide2);
        List<Ride> acceptedRides = List.of(acceptedRide1, acceptedRide2);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(Optional.of(pendingRides));
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(Optional.of(acceptedRides));

        Collection<Ride> rides = new ArrayList<>();
        rides.addAll(pendingRides);
        rides.addAll(acceptedRides);

        List<RideDto> expectedScheduledRidesDto = rides.stream().map(RideDto::new).toList();
        expectedScheduledRidesDto = expectedScheduledRidesDto.stream().sorted(Comparator.comparing((RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat))).toList();

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());

        assertEquals(expectedScheduledRidesDto, actualScheduledRides);

    }

    @Test
    public void getScheduledRidesForDriverAsDto_shouldReturnEmptyListWhenDriverNotHaveScheduledRides() throws IOException, DriverNotFoundException, RideNotFoundException {
        Driver driver = customTestData.getDriver();

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(Optional.of(new ArrayList<>()));
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(Optional.of(new ArrayList<>()));

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());
        assertEquals(new ArrayList<>(), actualScheduledRides);
    }


    // setDriver
    @Test
    public void setDriver_shouldSetDriver() throws IOException, DriverNotFoundException, RideNotFoundException {
        Ride ride = customTestData.getPendingRide();
        ride.setDriver(null);

        Driver driver = customTestData.getDriver();
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
            Driver driver = customTestData.getDriver();
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
            Ride acceptedRide = customTestData.getAcceptedRide();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            rideService.startRide(1L, "");
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }

    @Test
    public void startRide_shouldThrowDriverNotFoundWhenDriverRideIdAndFoundDriverIdNotSame() throws RideCancelationException, IOException, DriverNotFoundException {
        try {

            Ride acceptedRide = customTestData.getAcceptedRide();
            Driver driver = customTestData.getDriver();
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
            Ride rejectedRide = customTestData.getRejectedRide();
            Driver driver = customTestData.getDriver();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(rejectedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.startRide(1L, "");
        } catch (RideCancelationException e) {
            assertEquals("Cannot start a ride that is not in status ACCEPTED!", e.getMessage());
        }
    }

    @Test
    public void startRide_shouldStartRide() throws IOException, DriverNotFoundException, RideCancelationException, RideNotFoundException {
        Ride acceptedRide = customTestData.getAcceptedRide();
        Driver driver = customTestData.getDriver();
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
            Ride acceptedRide = customTestData.getAcceptedRide();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            rideService.rejectRide(1L, "", null);
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }

    @Test
    public void rejectRide_shouldThrowDriverNotFoundWhenDriverRideIdAndFoundDriverIdNotSame() throws RideCancelationException, IOException, DriverNotFoundException {
        try {

            Ride acceptedRide = customTestData.getAcceptedRide();
            Driver driver = customTestData.getDriver();
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
            Ride rejectedRide = customTestData.getRejectedRide();
            Driver driver = customTestData.getDriver();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(rejectedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.rejectRide(1L, "", null);
        } catch (RideCancelationException e) {
            assertEquals("Cannot cancel a ride that is not in status PENDING or ACCEPTED!", e.getMessage());
        }
    }

    @Test
    public void rejectRide_shouldRejectAcceptedRide() throws IOException, DriverNotFoundException, RideCancelationException, RideNotFoundException {
        Ride acceptedRide = customTestData.getAcceptedRide();
        Driver driver = customTestData.getDriver();
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
    public void rejectRide_shouldRejectPendingRide() throws IOException, DriverNotFoundException, RideCancelationException, RideNotFoundException {
        Ride pendingRide = customTestData.getPendingRide();
        Driver driver = customTestData.getDriver();
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
    public void endRide_shouldThrowRideNotFoundExceptionForNonExistingRideId() throws DriverNotFoundException, RideCancelationException {
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
    public void endRide_shouldThrowDriverNotFoundForNonExistingDriverId() throws RideCancelationException, IOException, RideNotFoundException {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(driverService).getByEmailAddress(Mockito.anyString());
        try {
            Ride acceptedRide = customTestData.getAcceptedRide();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            rideService.endRide(1L, "");
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }

    @Test
    public void endRide_shouldThrowDriverNotFoundWhenDriverRideIdAndFoundDriverIdNotSame() throws RideCancelationException, IOException, DriverNotFoundException {
        try {

            Ride acceptedRide = customTestData.getAcceptedRide();
            Driver driver = customTestData.getDriver();
            driver.setId(123L);
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(acceptedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.endRide(1L, "");
        } catch (RideNotFoundException e) {
            assertEquals("Ride does not exist!", e.getMessage());
        }
    }

    @Test
    public void endRide_shouldThrowRideCancellationExceptionWhenRideStatusIsNotAActive() throws IOException, DriverNotFoundException, RideNotFoundException {
        try {
            Ride rejectedRide = customTestData.getRejectedRide();
            Driver driver = customTestData.getDriver();
            Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(rejectedRide));
            Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
            rideService.endRide(1L, "");
        } catch (RideCancelationException e) {
            assertEquals("Cannot end a ride that is not in status STARTED!", e.getMessage());
        }
    }

    @Test
    public void endRide_shouldEndRide() throws IOException, DriverNotFoundException, RideCancelationException, RideNotFoundException {
        Ride activeRide = customTestData.getActiveRide();
        Driver driver = customTestData.getDriver();
        Mockito.when(rideRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(activeRide));
        Mockito.when(driverService.getByEmailAddress(Mockito.anyString())).thenReturn(Optional.of(driver));
        lenient().doNothing().when(driverService).save(driver);


        RideDto actualRideDto = rideService.endRide(1L, "");

        activeRide.setStatus(Enums.RideStatus.FINISHED);
        activeRide.setEndTime(LocalDateTime.now());
        RideDto expectedRideDto = new RideDto(activeRide);
        assertEquals(expectedRideDto, actualRideDto);
    }

    @Test
    public void createFavoriteLocation_whenNull() throws IOException, UserNotFoundException, PassengerNotFoundException, TooManyFavoriteRidesException {
//        Mockito.when(favoriteLocationService.save(any(FavoriteLocation)))
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            rideService.createFavoriteLocation(null, null);
        });
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    public void createFavoriteLocation_nonExistingPassenger() throws IOException, UserNotFoundException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        Passenger passenger = customTestData.getPassenger1();

        Mockito.when(passengerService.findByEmailAddress(anyString())).thenThrow(UserNotFoundException.class);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            rideService.createFavoriteLocation(passenger.getEmailAddress(), new FavoriteLocationDto(favoriteLocation));
        });

        assertEquals(UserNotFoundException.class, exception.getClass());
    }

    @Test
    public void checkIfMoreThan9FavoriteLocations_tooManyRidesException() throws IOException, UserNotFoundException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        Passenger passenger = customTestData.getPassenger1();
        List<FavoriteLocation>favoriteLocations = new ArrayList<>();
        for(int i = 0; i < 15; i++){
            favoriteLocations.add(new FavoriteLocation());
        }

        Mockito.when(passengerService.findByEmailAddress(anyString())).thenReturn(passenger);
        Mockito.when(favoriteLocationService.findByPassengerId(passenger.getId())).thenReturn(favoriteLocations);

        TooManyFavoriteRidesException exception = assertThrows(TooManyFavoriteRidesException.class, ()->{
            rideService.createFavoriteLocation(passenger.getEmailAddress(), new FavoriteLocationDto(favoriteLocation));
        });

        assertEquals(TooManyFavoriteRidesException.class, exception.getClass());
    }

    @Test
    public void checkIfMoreThan9FavoriteLocations_whenPassengerNull() throws IOException, UserNotFoundException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        Passenger passenger = customTestData.getPassenger1();
        List<FavoriteLocation>favoriteLocations = new ArrayList<>();

        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            rideService.createFavoriteLocation(null, new FavoriteLocationDto(favoriteLocation));
        });

        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    public void checkIfMoreThan9FavoriteLocations_whenFavoriteLocationNull() throws IOException, UserNotFoundException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        Passenger passenger = customTestData.getPassenger1();
        List<FavoriteLocation>favoriteLocations = new ArrayList<>();

        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            rideService.createFavoriteLocation(null, new FavoriteLocationDto(favoriteLocation));
        });

        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    public void addPassengerFromFavoriteLocation_null() throws UserNotFoundException {
        assertEquals(new HashSet<>(),rideService.addPassengerFromFavoriteLocation(null));
    }

    @Test void addPassengersFromFavoriteLocation_happyCase() throws IOException, UserNotFoundException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        Passenger passenger = customTestData.getPassenger1();
        Mockito.when(passengerService.findById(anyLong())).thenReturn(passenger);
        assertEquals(favoriteLocation.getPassengers().size(), rideService.addPassengerFromFavoriteLocation(new FavoriteLocationDto(favoriteLocation)).size());
    }

    @Test
    public void createFavoriteLocation_happyCase() throws IOException, UserNotFoundException, PassengerNotFoundException, TooManyFavoriteRidesException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(favoriteLocation);
        Passenger passenger = customTestData.getPassenger1();
        Set<Passenger>passengers = new HashSet<>();
        List<FavoriteLocation>favoriteLocations = new ArrayList<>();
        for(int i = 0; i < favoriteLocation.getPassengers().size(); i++){
            passengers.add(passenger);
        }
        favoriteLocation.setPassengers(passengers);

        Mockito.when(passengerService.findByEmailAddress(passenger.getEmailAddress())).thenReturn(passenger);
        Mockito.when(favoriteLocationService.findByPassengerId(passenger.getId())).thenReturn(favoriteLocations);
        Mockito.when(favoriteLocationService.save(any(FavoriteLocation.class))).thenReturn(favoriteLocation);
        Mockito.when(vehicleTypeService.getByName(anyString())).thenReturn(favoriteLocation.getVehicleType());
        Mockito.when(passengerService.findById(anyLong())).thenReturn(passenger);

        assertEquals(new FavoriteLocationDto(favoriteLocation), rideService.createFavoriteLocation(passenger.getEmailAddress(), favoriteLocationDto));
    }

    @Test
    public void acceptRide_rideNotFound() throws DriverNotFoundException, RideCancelationException, RideNotFoundException {
        RideNotFoundException exception = assertThrows(RideNotFoundException.class, () -> {
            rideService.acceptRide(-1L, anyString());
        });
        assertEquals(RideNotFoundException.class, exception.getClass());
    }

    @Test
    public void acceptRide_driverNotFound() throws IOException {
        Ride ride = customTestData.getPendingRide();
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ride));
        Mockito.when(driverService.getByEmailAddress(anyString())).thenReturn(Optional.empty());

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> {
            rideService.acceptRide(-1L, anyString());
        });
        assertEquals(DriverNotFoundException.class, exception.getClass());
    }

    @Test
    public void acceptRide_rideNotPending() throws IOException {
        Ride ride = customTestData.getAcceptedRide();
        Driver driver = customTestData.getDriver();
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.ofNullable(ride));
        Mockito.when(driverService.getByEmailAddress(driver.getEmailAddress())).thenReturn(Optional.ofNullable(driver));
        RideCancelationException exception = assertThrows(RideCancelationException.class, () -> {
            rideService.acceptRide(ride.getId(), driver.getEmailAddress());
        });
        assertEquals(RideCancelationException.class, exception.getClass());
    }

    @Test
    public void acceptRide_happyCase() throws IOException, DriverNotFoundException, RideCancelationException, RideNotFoundException {
        Ride ride = customTestData.getPendingRide();
        Ride testRide = customTestData.getPendingRide();
        testRide.setStatus(Enums.RideStatus.ACCEPTED);
        Driver driver = customTestData.getDriver();

        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.ofNullable(ride));
        Mockito.when(driverService.getByEmailAddress(driver.getEmailAddress())).thenReturn(Optional.ofNullable(driver));
        Mockito.when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        assertEquals(new RideDto(testRide), rideService.acceptRide(ride.getId(), driver.getEmailAddress()));
    }

    @Test
    public void creatingPanicProcedure_userNotFound(){
        Mockito.when(userService.findByEmailAddress(anyString())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()->{
            rideService.creatingPanicProcedure(userService, panicService, "", -1L, null);
        });
        assertEquals(UserNotFoundException.class, exception.getClass());
    }

    @Test
    public void creatingPanicProcedure_rideNotFound() throws IOException {
        Passenger passenger = customTestData.getPassenger1();
        Mockito.when(userService.findByEmailAddress(anyString())).thenReturn(Optional.ofNullable(passenger));
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.empty());
        RideNotFoundException exception = assertThrows(RideNotFoundException.class, ()->{
            rideService.creatingPanicProcedure(userService, panicService, "", -1L, null);
        });
        assertEquals(RideNotFoundException.class, exception.getClass());
    }

    @Test
    public void creatingPanicProcedure_userServiceNull(){
        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            rideService.creatingPanicProcedure(null, panicService, "", -1L, null);
        });
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    public void creatingPanicProcedure_panicServiceNull() throws IOException {
        Passenger passenger = customTestData.getPassenger1();
        Ride ride = customTestData.getActiveRide();
        Mockito.when(userService.findByEmailAddress(anyString())).thenReturn(Optional.ofNullable(passenger));
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ride));
        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            rideService.creatingPanicProcedure(userService, null, "", -1L, null);
        });
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    public void creatingPanicProcedure_happyCase() throws IOException, UserNotFoundException, RideNotFoundException {
        Passenger passenger = customTestData.getPassenger1();
        Ride ride = customTestData.getActiveRide();
        PanicCreateDto reason = new PanicCreateDto();
        reason.setReason("Razlog");
        Panic panic = new Panic(reason, ride, passenger);

        Mockito.when(userService.findByEmailAddress(anyString())).thenReturn(Optional.ofNullable(passenger));
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ride));

        assertEquals(new PanicDetailsDto(panic), rideService.creatingPanicProcedure(userService, panicService,passenger.getEmailAddress(), ride.getId(), reason));
    }

    @Test
    public void cancelRideById(){
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.empty());
        RideNotFoundException exception = assertThrows(RideNotFoundException.class, ()->{
            rideService.cancelRideById(-1L);
        });
        assertEquals(RideNotFoundException.class, exception.getClass());
    }

    @Test
    public void cancelRideById_rideIsNotPendingOrActive() throws IOException {
        Ride ride = customTestData.getFinishedRide();
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));
        RideCancelationException exception = assertThrows(RideCancelationException.class, ()->{
            rideService.cancelRideById(-1L);
        });
        assertEquals(RideCancelationException.class, exception.getClass());
    }

    @Test
    public void cancelRideById_happyCase() throws IOException, RideCancelationException, RideNotFoundException {
        Ride ride = customTestData.getPendingRide();
        Ride testRide = customTestData.getPendingRide();
        testRide.setStatus(Enums.RideStatus.REJECTED);

        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));

        assertEquals(new RideDto(testRide), rideService.cancelRideById(ride.getId()));
    }

    @Test
    public void getPassengersActiveRide_rideNotFound(){
        Mockito.when(rideRepository.findByPassengersIdAndStatus(anyLong(), anyInt())).thenReturn(Optional.empty());
        RideNotFoundException exception = assertThrows(RideNotFoundException.class, ()->{
            rideService.getPassengersActiveRide(1L);
        });
        assertEquals(RideNotFoundException.class, exception.getClass());
    }

    @Test
    public void getPassengersActiveRide_activeRideNotFound(){
        List<Ride> rides = new ArrayList<>();
        Mockito.when(rideRepository.findByPassengersIdAndStatus(anyLong(), anyInt())).thenReturn(Optional.of(rides));
        RideNotFoundException exception = assertThrows(RideNotFoundException.class, ()->{
            rideService.getPassengersActiveRide(1L);
        });
        assertEquals(RideNotFoundException.class, exception.getClass());
    }

    @Test
    public void getPassengersActiveRide_happyCase() throws IOException, RideNotFoundException {
        List<Ride> rides = new ArrayList<>();
        rides.add(customTestData.getActiveRide());
        Mockito.when(rideRepository.findByPassengersIdAndStatus(anyLong(), anyInt())).thenReturn(Optional.of(rides));
        assertEquals(rides.stream().map(RideDto::new).toList(), rideService.getPassengersActiveRide(rides.get(0).getId()));
    }

    @Test
    public void getDriversActiveRide_userNotFound() throws IOException, RideNotFoundException {
        Mockito.when(driverService.getById(anyLong())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()->{
            rideService.getDriversActiveRide(1L);
        });
        assertEquals(UserNotFoundException.class, exception.getClass());
    }

    @Test
    public void getDriversActiveRide_noActiveRideFound() throws IOException {
        Driver driver = customTestData.getDriverWithNoRides();
        Mockito.when(driverService.getById(anyLong())).thenReturn(Optional.ofNullable(driver));
        Mockito.when(rideRepository.findByDriverIdAndStatus(1L, Enums.RideStatus.ACTIVE.ordinal())).thenReturn(Optional.of(new ArrayList<>()));

        RideNotFoundException exception = assertThrows(RideNotFoundException.class, ()->{
            rideService.getDriversActiveRide(1L);
        });
        assertEquals(RideNotFoundException.class, exception.getClass());
    }

    @Test
    public void getDriversActiveRide_happyCase() throws IOException, UserNotFoundException, RideNotFoundException {
        Driver driver = customTestData.getDriverWithNoRides();
        List<Ride>rides = new ArrayList<>();
        rides.add(customTestData.getActiveRide());

        Mockito.when(driverService.getById(anyLong())).thenReturn(Optional.ofNullable(driver));
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACTIVE.ordinal())).thenReturn(Optional.of(rides));

        assertEquals(rides.stream().map(RideDto::new).toList(), rideService.getDriversActiveRide(driver.getId()));
    }

    @Test
    public void deleteFavoriteLocationService_noFavoriteLocationFound(){
        Mockito.when(favoriteLocationService.findById(anyLong())).thenReturn(null);
        FavoriteLocationNotFoundException exception = assertThrows(FavoriteLocationNotFoundException.class, () ->{
            rideService.deleteFavoriteLocation(1L);
        });
        assertEquals(FavoriteLocationNotFoundException.class, exception.getClass());
    }

    @Test
    public void getFavoriteLocationByPassengerId_noPassengerFound() throws UserNotFoundException {
        Mockito.when(favoriteLocationService.findByPassengerId(anyLong())).thenThrow(UserNotFoundException.class);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()->{
            rideService.getFavoriteLocationsByPassengerId(1L);
        });
        assertEquals(UserNotFoundException.class, exception.getClass());
    }

    @Test
    public void getFavoriteLocationByPassengerId_happyCase() throws UserNotFoundException, IOException {
        List<FavoriteLocation> favoriteLocations = new ArrayList<>();
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        favoriteLocations.add(favoriteLocation);

        Mockito.when(favoriteLocationService.findByPassengerId(anyLong())).thenReturn(favoriteLocations);

        assertEquals(favoriteLocations.stream().map(FavoriteLocationDto::new).toList(), rideService.getFavoriteLocationsByPassengerId(1L));
    }

    @Test
    public void getFavoriteLocationById_favoriteLocationNotFound(){
        Mockito.when(favoriteLocationService.findById(anyLong())).thenReturn(null);
        FavoriteLocationNotFoundException exception = assertThrows(FavoriteLocationNotFoundException.class, () ->{
            rideService.deleteFavoriteLocation(1L);
        });
        assertEquals(FavoriteLocationNotFoundException.class, exception.getClass());
    }

    @Test
    public void getFavoriteLocationById_happyCase() throws IOException, FavoriteLocationNotFoundException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();

        Mockito.when(favoriteLocationService.findById(anyLong())).thenReturn(favoriteLocation);

        assertEquals(new FavoriteLocationDto(favoriteLocation), rideService.getFavoriteLocationById(1L));
    }

}
