package com.tim7.iss.tim7iss;


import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import com.tim7.iss.tim7iss.services.*;
import net.bytebuddy.asm.Advice;
import org.aspectj.apache.bcel.classfile.ConstantString;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
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
    private FavoriteLocationService favoriteLocationService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @Mock
    private WorkHourService workHourService;

    @Mock
    private MapService mapService;
    @Mock
    private PassengerService passengerService;

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
        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(List.of(ride));

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
        }).when(userRepository).findById(anyLong());
        try {
            rideService.getAllFinishedRides(anyLong());
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
        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(List.of(ride));

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
        }).when(userRepository).findById(anyLong());
        try {
            rideService.getAllRejectedRides(anyLong());
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

        Mockito.when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findByDriverId(anyLong())).thenReturn(List.of(ride));


        PaginatedResponseDto expectedPaginatedResponseDto = new PaginatedResponseDto(List.of(rideDto).size(), List.of(rideDto));

        PaginatedResponseDto actualPaginatedResponseDto = rideService.getPaginatedRidesForDriverAsDto(anyLong(), null);

        assertEquals(expectedPaginatedResponseDto, actualPaginatedResponseDto);
    }

    @Test
    public void getPaginatedRidesForDriverAsDto_shouldReturnPaginatedRidesWhenDriverHasNoRides() throws IOException, DriverNotFoundException {
        Driver driver = customTestData.getDriver();


        Mockito.when(driverRepository.findById(anyLong())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findByDriverId(anyLong())).thenReturn(new ArrayList<>());


        PaginatedResponseDto expectedPaginatedResponseDto = new PaginatedResponseDto(0, new ArrayList());

        PaginatedResponseDto actualPaginatedResponseDto = rideService.getPaginatedRidesForDriverAsDto(anyLong(), null);

        assertEquals(expectedPaginatedResponseDto, actualPaginatedResponseDto);
    }

    @Test
    public void getPaginatedRidesForDriverAsDto_shouldThrowDriverNotFoundExceptionForNonExistingDriver() {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(driverRepository).findById(anyLong());
        try {
            rideService.getPaginatedRidesForDriverAsDto(anyLong(), null);
        } catch (DriverNotFoundException e) {
            assertEquals("Driver does not exist!", e.getMessage());
        }
    }


    // getScheduledRidesForDriverAsDto
    @Test
    public void getScheduledRidesForDriverAsDto_shouldThrowDriverNotFoundFoundWhenIdOfDriverNotExist() throws RideNotFoundException {
        doAnswer(invocation -> {
            throw new DriverNotFoundException();
        }).when(driverRepository).findById(anyLong());
        try {
            rideService.getScheduledRidesForDriverAsDto(anyLong());
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
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenOnlyHavePendingRides() throws IOException, DriverNotFoundException, RideNotFoundException {
        Driver driver = customTestData.getDriver();
        Ride ride1 = customTestData.getPendingRide();
        Ride ride2 = customTestData.getPendingRide();
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
    public void getScheduledRidesForDriverAsDto_shouldReturnEmptyListWhenDriverNotHaveScheduledRides() throws IOException, DriverNotFoundException, RideNotFoundException {
        Driver driver = customTestData.getDriver();

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.PENDING.ordinal())).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACCEPTED.ordinal())).thenReturn(new ArrayList<>());

        Collection<RideDto> actualScheduledRides = rideService.getScheduledRidesForDriverAsDto(driver.getId());
        assertEquals(new ArrayList<>(), actualScheduledRides);
    }


    // setDriver
    @Test
    public void setDriver_shouldSetDriver() throws IOException, DriverNotFoundException, RideNotFoundException {
        Ride ride = customTestData.getPendingRide();
        ride.setDriver(null);

        Driver driver = customTestData.getDriver();
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(ride));
        Mockito.when(driverService.findById(anyLong())).thenReturn(driver);
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
        }).when(rideRepository).findById(anyLong());
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
        }).when(rideRepository).findById(anyLong());
        try {
            Driver driver = customTestData.getDriver();
            lenient().when(driverService.findById(anyLong())).thenReturn(driver);
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
        }).when(rideRepository).findById(anyLong());
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(acceptedRide));
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(acceptedRide));
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(rejectedRide));
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
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(acceptedRide));
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
        }).when(rideRepository).findById(anyLong());
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(acceptedRide));
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(acceptedRide));
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(rejectedRide));
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
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(acceptedRide));
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
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(pendingRide));
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
        }).when(rideRepository).findById(anyLong());
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(acceptedRide));
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(acceptedRide));
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
            Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(rejectedRide));
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
        Mockito.when(rideRepository.findById(anyLong())).thenReturn(Optional.of(activeRide));
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
        assertThrows(PassengerNotFoundException.class, () -> {
            rideService.createFavoriteLocation(null, null);
        });
    }

    @Test
    public void createFavoriteLocation_nonExistingPassenger() throws IOException, UserNotFoundException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        Passenger passenger = customTestData.getPassenger1();

        Mockito.when(passengerRepository.findByEmailAddress(anyString())).thenReturn(Optional.empty());

        assertThrows(PassengerNotFoundException.class, () -> {
            rideService.createFavoriteLocation(passenger.getEmailAddress(), new FavoriteLocationDto(favoriteLocation));
        });
    }

    @Test
    public void addPassengerFromFavoriteLocation_null() throws UserNotFoundException {
        assertEquals(new HashSet<>(),rideService.addPassengerFromFavoriteLocation(null));
    }

    @Test
    public void addPassengersFromFavoriteLocation_happyCase() throws IOException, UserNotFoundException {
        FavoriteLocation favoriteLocation = customTestData.getFavoriteLocation();
        Passenger passenger = customTestData.getPassenger1();
        Mockito.when(passengerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(passenger));
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

        Mockito.when(passengerRepository.findByEmailAddress(passenger.getEmailAddress())).thenReturn(Optional.of(passenger));
        Mockito.when(favoriteLocationService.findByPassengerId(passenger.getId())).thenReturn(favoriteLocations);
        Mockito.when(favoriteLocationService.save(any(FavoriteLocation.class))).thenReturn(favoriteLocation);
        Mockito.when(vehicleTypeRepository.findByName(anyString())).thenReturn(Optional.ofNullable(favoriteLocation.getVehicleType()));
        Mockito.when(passengerRepository.findById(anyLong())).thenReturn(Optional.of(passenger));

        FavoriteLocationDto favoriteLocationDto1 = rideService.createFavoriteLocation(passenger.getEmailAddress(), favoriteLocationDto);
        favoriteLocation.setId(favoriteLocationDto1.getId());
        assertEquals(new FavoriteLocationDto(favoriteLocation), favoriteLocationDto1);
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
        Mockito.when(rideRepository.findByPassengersIdAndStatus(anyLong(), anyInt())).thenReturn(new ArrayList<>());
        RideNotFoundException exception = assertThrows(RideNotFoundException.class, ()->{
            rideService.getPassengersActiveRide(1L);
        });
        assertEquals(RideNotFoundException.class, exception.getClass());
    }

    @Test
    public void getPassengersActiveRide_activeRideNotFound(){
        List<Ride> rides = new ArrayList<>();
        Mockito.when(rideRepository.findByPassengersIdAndStatus(anyLong(), anyInt())).thenReturn(rides);
        RideNotFoundException exception = assertThrows(RideNotFoundException.class, ()->{
            rideService.getPassengersActiveRide(1L);
        });
        assertEquals(RideNotFoundException.class, exception.getClass());
    }

    @Test
    public void getPassengersActiveRide_happyCase() throws IOException, RideNotFoundException {
        List<Ride> rides = new ArrayList<>();
        rides.add(customTestData.getActiveRide());
        Mockito.when(rideRepository.findByPassengersIdAndStatus(anyLong(), anyInt())).thenReturn(rides);
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
        Mockito.when(rideRepository.findByDriverIdAndStatus(1L, Enums.RideStatus.ACTIVE.ordinal())).thenReturn(new ArrayList<>());

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
        Mockito.when(rideRepository.findByDriverIdAndStatus(driver.getId(), Enums.RideStatus.ACTIVE.ordinal())).thenReturn(rides);

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

    @Test
    public void driverRideAtMoment_ShouldReturnRideIfDriverHasAcceptedRideAtThatMoment() {
        LocalDateTime timeOfRide = LocalDateTime.of(2023, Month.MAY, 19, 8, 3);

        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getId()).thenReturn(1L);

        Ride acceptedRide = Mockito.mock(Ride.class);
        Mockito.when(acceptedRide.getStartTime()).thenReturn(timeOfRide);
        Mockito.when(acceptedRide.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(acceptedRide.getStatus()).thenReturn(Enums.RideStatus.ACCEPTED);

        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(List.of(acceptedRide));
        Ride rideAtMoment = rideService.driverRideAtMoment(driver.getId(), timeOfRide.plusMinutes(1));
        assertNotNull(rideAtMoment);
    }

    @Test void driverRideAtMoment_ShouldReturnRideIfDriverHasPendingRideAtThatMoment() {
        LocalDateTime timeOfRide = LocalDateTime.of(2023, Month.MAY, 19, 8, 3);

        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getId()).thenReturn(1L);

        Ride pendingRide = Mockito.mock(Ride.class);
        Mockito.when(pendingRide.getStartTime()).thenReturn(timeOfRide);
        Mockito.when(pendingRide.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(pendingRide.getStatus()).thenReturn(Enums.RideStatus.PENDING);

        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(List.of(pendingRide));

        Ride rideAtMoment = rideService.driverRideAtMoment(driver.getId(), timeOfRide);
        assertNotNull(rideAtMoment);
    }

    @Test void driverRideAtMoment_ShouldReturnRideIfDriverHasActiveRideAtThatMoment() {
        LocalDateTime timeOfRide = LocalDateTime.of(2023, Month.MAY, 19, 8, 3);

        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getId()).thenReturn(1L);

        Ride activeRide = Mockito.mock(Ride.class);
        Mockito.when(activeRide.getStartTime()).thenReturn(timeOfRide);
        Mockito.when(activeRide.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(activeRide.getStatus()).thenReturn(Enums.RideStatus.ACTIVE);

        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(List.of(activeRide));

        Ride rideAtMoment = rideService.driverRideAtMoment(driver.getId(), timeOfRide);
        assertNotNull(rideAtMoment);
    }

    @Test void driverRideAtMoment_ShouldNotReturnRideIfDriverHasNoActivePendingOrAcceptedRidesAtThatMoment() {
        LocalDateTime timeOfRide = LocalDateTime.of(2023, Month.MAY, 19, 8, 3);

        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getId()).thenReturn(1L);

        Ride acceptedRide = Mockito.mock(Ride.class);
        Mockito.when(acceptedRide.getStartTime()).thenReturn(timeOfRide.plusDays(1));
        Mockito.when(acceptedRide.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(acceptedRide.getStatus()).thenReturn(Enums.RideStatus.ACCEPTED);

        Ride pendingRide = Mockito.mock(Ride.class);
        Mockito.when(pendingRide.getStartTime()).thenReturn(timeOfRide.plusDays(2));
        Mockito.when(pendingRide.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(pendingRide.getStatus()).thenReturn(Enums.RideStatus.PENDING);

        Ride activeRide = Mockito.mock(Ride.class);
        Mockito.when(activeRide.getStartTime()).thenReturn(timeOfRide.plusDays(3));
        Mockito.when(activeRide.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(activeRide.getStatus()).thenReturn(Enums.RideStatus.ACTIVE);

        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(
                List.of(acceptedRide, pendingRide, activeRide));

        Ride rideAtMoment = rideService.driverRideAtMoment(driver.getId(), timeOfRide);
        assertNull(rideAtMoment);
    }

    @Test void driverRideAtMoment_ShouldNotReturnRideIfDriverHasOnlyFinishedRejectedOrCancelledRidesAtThatMoment() {
        LocalDateTime timeOfRide = LocalDateTime.of(2023, Month.MAY, 19, 8, 3);

        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getId()).thenReturn(1L);

        Ride finishedRide = Mockito.mock(Ride.class);
        Mockito.when(finishedRide.getStatus()).thenReturn(Enums.RideStatus.FINISHED);

        Ride rejectedRide = Mockito.mock(Ride.class);
        Mockito.when(rejectedRide.getStatus()).thenReturn(Enums.RideStatus.REJECTED);

        Ride cancelledRide = Mockito.mock(Ride.class);
        Mockito.when(cancelledRide.getStatus()).thenReturn(Enums.RideStatus.CANCELED);

        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(
                List.of(finishedRide, rejectedRide, cancelledRide));
        Ride rideAtMoment = rideService.driverRideAtMoment(driver.getId(), timeOfRide);
        assertNull(rideAtMoment);
    }

    @Test
    public void driverRideAtMoment_ShouldNotReturnRideIfDriverHasNoRides() {
        LocalDateTime timeOfRide = LocalDateTime.of(2023, Month.MAY, 19, 8, 3);

        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getId()).thenReturn(1L);

        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(new ArrayList<>());
        Ride rideAtMoment = rideService.driverRideAtMoment(driver.getId(), timeOfRide);
        assertNull(rideAtMoment);
    }

    @Test
    public void AnyRidesArePending_ShouldReturnTrueIfAnyPassengerHasPendingRides() throws RideNotFoundException {
        UserRefDto passenger1 = Mockito.mock(UserRefDto.class);
        Mockito.when(passenger1.getId()).thenReturn(1L);

        List<UserRefDto> passengers = List.of(passenger1);

        Ride pendingRide = Mockito.mock(Ride.class);

        Mockito.when(rideRepository.findByPassengersIdAndStatus(passenger1.getId(), Enums.RideStatus.PENDING.ordinal()))
                .thenReturn(List.of(pendingRide));
        assertEquals(true, rideService.AnyRidesArePending(passengers));
    }

    @Test
    public void AnyRidesArePending_ShouldReturnFalseIfNoPassengersHaveNoPendingRides() throws IOException, RideNotFoundException {
        UserRefDto passenger1 = Mockito.mock(UserRefDto.class);
        Mockito.when(passenger1.getId()).thenReturn(1L);
        UserRefDto passenger2 = Mockito.mock(UserRefDto.class);
        Mockito.when(passenger2.getId()).thenReturn(2L);
        UserRefDto passenger3 = Mockito.mock(UserRefDto.class);
        Mockito.when(passenger3.getId()).thenReturn(3L);
        List<UserRefDto> passengers = List.of(passenger1, passenger2, passenger3);

        Mockito.when(rideRepository.findByPassengersIdAndStatus(passenger1.getId(), Enums.RideStatus.PENDING.ordinal()))
                        .thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findByPassengersIdAndStatus(passenger2.getId(), Enums.RideStatus.PENDING.ordinal()))
                .thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findByPassengersIdAndStatus(passenger3.getId(), Enums.RideStatus.PENDING.ordinal()))
                .thenReturn(new ArrayList<>());

        assertEquals(false, rideService.AnyRidesArePending(passengers));
    }

    @Test
    public void checkIfMoreThan9FavoriteLocations_ShouldReturnTrueIfPassengerHasMoreThanNineFavoriteLocations() throws UserNotFoundException {
        Passenger passenger = Mockito.mock(Passenger.class);
        Mockito.when(passenger.getId()).thenReturn(1L);

        List<FavoriteLocation> favoriteLocations = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            favoriteLocations.add(Mockito.mock(FavoriteLocation.class));
        }

        Mockito.when(favoriteLocationService.findByPassengerId(passenger.getId())).thenReturn(favoriteLocations);

        assertEquals(true, rideService.checkIfMoreThan9FavoriteLocations(passenger.getId()));

    }

    @Test
    public void checkIfMoreThan9FavoriteLocations_ShouldReturnFalseIfPassengerHasLessThanNineFavoriteLocations() throws UserNotFoundException {
        Passenger passenger = Mockito.mock(Passenger.class);
        Mockito.when(passenger.getId()).thenReturn(1L);

        List<FavoriteLocation> favoriteLocations = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            favoriteLocations.add(Mockito.mock(FavoriteLocation.class));
        }

        Mockito.when(favoriteLocationService.findByPassengerId(passenger.getId())).thenReturn(favoriteLocations);

        assertEquals(false, rideService.checkIfMoreThan9FavoriteLocations(passenger.getId()));
    }

    @Test
    public void createFavoriteLocation_ThrowsPassengerNotFoundErrorIfInvalidEmailIsPassed() {
        Passenger passenger = Mockito.mock(Passenger.class);
        Mockito.when(passenger.getEmailAddress()).thenReturn("email.that.does.not.exist@email.com");

        FavoriteLocationDto favoriteLocationDto = Mockito.mock(FavoriteLocationDto.class);
        Mockito.when(passengerRepository.findByEmailAddress(passenger.getEmailAddress())).thenReturn(Optional.ofNullable(null));

        assertThrows(PassengerNotFoundException.class, () -> rideService.createFavoriteLocation(passenger.getEmailAddress(), favoriteLocationDto));
    }

    @Test
    public void createFavoriteLocation_ThrowsTooManyFavoriteLocationException() throws UserNotFoundException {
        Passenger passenger = Mockito.mock(Passenger.class);
        Mockito.when(passenger.getId()).thenReturn(1L);
        Mockito.when(passenger.getEmailAddress()).thenReturn("email.that.exists@email.com");

        Mockito.when(passengerRepository.findByEmailAddress(passenger.getEmailAddress())).thenReturn(Optional.of(passenger));

        List<FavoriteLocation> favoriteLocations = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            favoriteLocations.add(Mockito.mock(FavoriteLocation.class));
        }
        Mockito.when(favoriteLocationService.findByPassengerId(passenger.getId())).thenReturn(favoriteLocations);

        FavoriteLocationDto favoriteLocationDto = Mockito.mock(FavoriteLocationDto.class);
        assertThrows(TooManyFavoriteRidesException.class, () -> rideService.createFavoriteLocation(passenger.getEmailAddress(), favoriteLocationDto));
    }

    @Test
    public void createFavoriteLocation_ShouldCreateFavoriteLocationWithValidData() throws UserNotFoundException, PassengerNotFoundException, TooManyFavoriteRidesException {
        Passenger passenger = Mockito.mock(Passenger.class);
        Mockito.when(passenger.getId()).thenReturn(1L);
        Mockito.when(passenger.getEmailAddress()).thenReturn("email.that.exists@email.com");
        UserRefDto passengerRefDto = Mockito.mock(UserRefDto.class);
        Mockito.when(passengerRefDto.getId()).thenReturn(1L);

        RouteDto routeDto = Mockito.mock(RouteDto.class);
        Mockito.when(routeDto.getDeparture()).thenReturn(Mockito.mock(GeoCoordinateDto.class));
        Mockito.when(routeDto.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        FavoriteLocationDto favoriteLocationDto = Mockito.mock(FavoriteLocationDto.class);
        Mockito.when(favoriteLocationDto.getFavoriteName()).thenReturn("Home - Work");
        Mockito.when(favoriteLocationDto.getLocations()).thenReturn(Set.of(routeDto));
        Mockito.when(favoriteLocationDto.getPassengers()).thenReturn(Set.of(passengerRefDto));
        Mockito.when(favoriteLocationDto.getVehicleType()).thenReturn("STANDARD");

        Mockito.when(passengerRepository.findByEmailAddress(passenger.getEmailAddress())).thenReturn(Optional.of(passenger));
        Mockito.when(passengerRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));

        VehicleType vehicleType = Mockito.mock(VehicleType.class);
        Mockito.when(vehicleType.getName()).thenReturn("STANDARD");
        Mockito.when(vehicleTypeRepository.findByName(favoriteLocationDto.getVehicleType())).thenReturn(Optional.of(vehicleType));

        FavoriteLocationDto response = rideService.createFavoriteLocation(passenger.getEmailAddress(), favoriteLocationDto);
        assertNotNull(response);
        assertNotNull(response.getFavoriteName());
        assertNotNull(response.getLocations());
        assertNotNull(response.getPassengers());
        assertNotNull(response.getVehicleType());
    }

    @Test
    public void getFavoriteLocations_ShouldReturnNotEmptyListIfFavoriteLocationServiceDotGetAllReturnsNotEmptyList() {
        VehicleType vehicleType = Mockito.mock(VehicleType.class);
        Mockito.when(vehicleType.getName()).thenReturn("STANDARD");

        FavoriteLocation favoriteLocation = Mockito.mock(FavoriteLocation.class);
        Mockito.when(favoriteLocation.getRoutes()).thenReturn(new HashSet<>());
        Mockito.when(favoriteLocation.getPassengers()).thenReturn(new HashSet<>());
        Mockito.when(favoriteLocation.getVehicleType()).thenReturn(vehicleType);

        Mockito.when(favoriteLocationService.getAll()).thenReturn(List.of(favoriteLocation));
        assertTrue(rideService.getFavoriteLocations().size() > 0);
    }

    @Test
    public void getFavoriteLocations_ShouldReturnEmptyListIfFavoriteLocationServiceDotGetAllReturnsEmptyList() {
        Mockito.when(favoriteLocationService.getAll()).thenReturn(List.of());
        assertEquals(0, rideService.getFavoriteLocations().size());
    }

    @Test
    public void scheduleRide_ShouldScheduleRideIfDriverIsActiveAndHasNoRidesAtThatTime() throws DriverNotFoundException, SchedulingRideAtInvalidDateException, RideAlreadyPendingException, RideNotFoundException {
        Location location = Mockito.mock(Location.class);
        Mockito.when(location.getLongitude()).thenReturn(19.0f);
        Mockito.when(location.getLatitude()).thenReturn(45.0f);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        Mockito.when(vehicle.getLocation()).thenReturn(location);

        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getId()).thenReturn(1L);
        Mockito.when(driver.isActive()).thenReturn(true);
        Mockito.when(driver.getVehicle()).thenReturn(vehicle);

        Mockito.when(driverRepository.findAll()).thenReturn(List.of(driver));

        Mockito.when(workHourService.hoursWorked(driver.getId(), LocalDate.now())).thenReturn(5);

        Mockito.when(mapService.getDistance(anyFloat(), anyFloat(), anyFloat(), anyFloat())).thenReturn(100);

        Mockito.when(passengerRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Passenger.class)));

        VehicleType vehicleType = Mockito.mock(VehicleType.class);
        Mockito.when(vehicleType.getPricePerKm()).thenReturn(120);
        Mockito.when(vehicleType.getName()).thenReturn("STANDARD");
        Mockito.when(vehicleTypeRepository.findByName("STANDARD")).thenReturn(Optional.of(vehicleType));

        LocationForRideDto locationForRideDto = Mockito.mock(LocationForRideDto.class);
        Mockito.when(locationForRideDto.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(locationForRideDto.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(locationForRideDto.getDeparture()).thenReturn(Mockito.mock(GeoCoordinateDto.class));
        Mockito.when(locationForRideDto.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().plusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(locationForRideDto));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getVehicleType()).thenReturn("STANDARD");
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        RideDto rideDto = rideService.scheduleRide(rideCreationDto);
        assertEquals(rideCreationDto.getScheduledTime(), rideDto.getStartTime());
        assertEquals(locationForRideDto.getEstimatedTimeInMinutes(), rideDto.getEstimatedTimeInMinutes());
        assertEquals(rideCreationDto.getVehicleType(), rideDto.getVehicleType());
        assertEquals(rideCreationDto.getBabyTransport(), rideDto.getBabyTransport());
        assertEquals(rideCreationDto.getPetTransport(), rideDto.getPetTransport());
        assertEquals(rideCreationDto.getPassengers().size(), rideDto.getPassengers().size());
        assertEquals(driver.getId(), rideDto.getDriver().getId());
    }

    @Test
    public void scheduleRide_ShouldScheduleRideIfDriverIsActiveAndHasRidesAtThatTimeButFinishesSoon() throws DriverNotFoundException, SchedulingRideAtInvalidDateException, RideAlreadyPendingException, RideNotFoundException {
        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.getId()).thenReturn(1L);
        Mockito.when(driver.isActive()).thenReturn(true);

        Location endLocation = Mockito.mock(Location.class);
        Mockito.when(endLocation.getLatitude()).thenReturn(45.0f);
        Mockito.when(endLocation.getLongitude()).thenReturn(19.0f);
        Route route = Mockito.mock(Route.class);
        Mockito.when(route.getEndPoint()).thenReturn(endLocation);

        Ride currentRideOfDriver = Mockito.mock(Ride.class);
        Mockito.when(currentRideOfDriver.getRoutes()).thenReturn(List.of(route));
        Mockito.when(currentRideOfDriver.getStatus()).thenReturn(Enums.RideStatus.ACTIVE);
        Mockito.when(currentRideOfDriver.getStartTime()).thenReturn(LocalDateTime.now());
        Mockito.when(currentRideOfDriver.getEstimatedTimeInMinutes()).thenReturn(33);
        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(List.of(currentRideOfDriver));

        Mockito.when(driverRepository.findAll()).thenReturn(List.of(driver));

        Mockito.when(workHourService.hoursWorked(driver.getId(), LocalDate.now())).thenReturn(5);

        Mockito.when(passengerRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Passenger.class)));

        VehicleType vehicleType = Mockito.mock(VehicleType.class);
        Mockito.when(vehicleType.getPricePerKm()).thenReturn(120);
        Mockito.when(vehicleType.getName()).thenReturn("STANDARD");
        Mockito.when(vehicleTypeRepository.findByName("STANDARD")).thenReturn(Optional.of(vehicleType));

        LocationForRideDto locationForRideDto = Mockito.mock(LocationForRideDto.class);
        Mockito.when(locationForRideDto.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(locationForRideDto.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(locationForRideDto.getDeparture()).thenReturn(Mockito.mock(GeoCoordinateDto.class));
        Mockito.when(locationForRideDto.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().plusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(locationForRideDto));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getVehicleType()).thenReturn("STANDARD");
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        RideDto rideDto = rideService.scheduleRide(rideCreationDto);
        String expectedStartTime = LocalDateTime
                .parse(rideCreationDto.getScheduledTime(), Constants.customDateTimeFormat)
                .plusMinutes(Constants.vehicleWaitTimeInMinutes)
                .format(Constants.customDateTimeFormat);
        assertEquals(expectedStartTime, rideDto.getStartTime());
        assertEquals(locationForRideDto.getEstimatedTimeInMinutes(), rideDto.getEstimatedTimeInMinutes());
        assertEquals(rideCreationDto.getVehicleType(), rideDto.getVehicleType());
        assertEquals(rideCreationDto.getBabyTransport(), rideDto.getBabyTransport());
        assertEquals(rideCreationDto.getPetTransport(), rideDto.getPetTransport());
        assertEquals(rideCreationDto.getPassengers().size(), rideDto.getPassengers().size());
        assertEquals(driver.getId(), rideDto.getDriver().getId());
    }

    @Test
    public void scheduleRide_ShouldScheduleRideIfDriverIsActiveAndHasNoRidesAtThatMomentButDriversCloserToDepartureAreChosen() throws DriverNotFoundException, SchedulingRideAtInvalidDateException, RideAlreadyPendingException, RideNotFoundException {
        Location driver1Location = Mockito.mock(Location.class);
        Mockito.when(driver1Location.getLongitude()).thenReturn(19.0f);
        Mockito.when(driver1Location.getLatitude()).thenReturn(45.0f);
        Vehicle vehicle1 = Mockito.mock(Vehicle.class);
        Mockito.when(vehicle1.getLocation()).thenReturn(driver1Location);

        Location driver2Location = Mockito.mock(Location.class);
        Mockito.when(driver2Location.getLongitude()).thenReturn(19.9f);
        Mockito.when(driver2Location.getLatitude()).thenReturn(45.9f);
        Vehicle vehicle2 = Mockito.mock(Vehicle.class);
        Mockito.when(vehicle2.getLocation()).thenReturn(driver2Location);

        GeoCoordinateDto departureLocation = Mockito.mock(GeoCoordinateDto.class);
        Mockito.when(departureLocation.getLongitude()).thenReturn(20.0f);
        Mockito.when(departureLocation.getLatitude()).thenReturn(46.0f);

        Driver driver1 = Mockito.mock(Driver.class);
        Mockito.when(driver1.getId()).thenReturn(1L);
        Mockito.when(driver1.isActive()).thenReturn(true);
        Mockito.when(driver1.getVehicle()).thenReturn(vehicle1);

        Driver driver2 = Mockito.mock(Driver.class);
        Mockito.when(driver2.getId()).thenReturn(2L);
        Mockito.when(driver2.isActive()).thenReturn(true);
        Mockito.when(driver2.getVehicle()).thenReturn(vehicle2);

        Mockito.when(mapService
                .getDistance(
                        departureLocation.getLatitude(),
                        departureLocation.getLongitude(),
                        driver1Location.getLatitude(),
                        driver1Location.getLongitude()))
                .thenReturn(100);
        Mockito.when(mapService
                        .getDistance(
                                departureLocation.getLatitude(),
                                departureLocation.getLongitude(),
                                driver2Location.getLatitude(),
                                driver2Location.getLongitude()))
                .thenReturn(50);

        Mockito.when(driverRepository.findAll()).thenReturn(List.of(driver1, driver2));

        Mockito.when(workHourService.hoursWorked(driver1.getId(), LocalDate.now())).thenReturn(5);
        Mockito.when(workHourService.hoursWorked(driver2.getId(), LocalDate.now())).thenReturn(5);

        Mockito.when(passengerRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Passenger.class)));

        VehicleType vehicleType = Mockito.mock(VehicleType.class);
        Mockito.when(vehicleType.getPricePerKm()).thenReturn(120);
        Mockito.when(vehicleType.getName()).thenReturn("STANDARD");
        Mockito.when(vehicleTypeRepository.findByName("STANDARD")).thenReturn(Optional.of(vehicleType));

        LocationForRideDto locationForRideDto = Mockito.mock(LocationForRideDto.class);
        Mockito.when(locationForRideDto.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(locationForRideDto.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(locationForRideDto.getDeparture()).thenReturn(departureLocation);
        Mockito.when(locationForRideDto.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().plusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(locationForRideDto));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getVehicleType()).thenReturn("STANDARD");
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        RideDto rideDto = rideService.scheduleRide(rideCreationDto);

        assertEquals(rideCreationDto.getScheduledTime(), rideDto.getStartTime());
        assertEquals(locationForRideDto.getEstimatedTimeInMinutes(), rideDto.getEstimatedTimeInMinutes());
        assertEquals(rideCreationDto.getVehicleType(), rideDto.getVehicleType());
        assertEquals(rideCreationDto.getBabyTransport(), rideDto.getBabyTransport());
        assertEquals(rideCreationDto.getPetTransport(), rideDto.getPetTransport());
        assertEquals(rideCreationDto.getPassengers().size(), rideDto.getPassengers().size());
        assertEquals(driver2.getId(), rideDto.getDriver().getId());
    }

    @Test
    public void scheduleRide_ShouldScheduleRideIfDriverIsActiveAndHasRidesAtThatMomentButFinishedSoonButDriversCloserToDepartureAreChosen() throws DriverNotFoundException, SchedulingRideAtInvalidDateException, RideAlreadyPendingException, RideNotFoundException {
        Location driver1Location = Mockito.mock(Location.class);
        Mockito.when(driver1Location.getLongitude()).thenReturn(19.0f);
        Mockito.when(driver1Location.getLatitude()).thenReturn(45.0f);

        Location driver2Location = Mockito.mock(Location.class);
        Mockito.when(driver2Location.getLongitude()).thenReturn(19.9f);
        Mockito.when(driver2Location.getLatitude()).thenReturn(45.9f);

        GeoCoordinateDto departureLocation = Mockito.mock(GeoCoordinateDto.class);
        Mockito.when(departureLocation.getLongitude()).thenReturn(20.0f);
        Mockito.when(departureLocation.getLatitude()).thenReturn(46.0f);

        Driver driver1 = Mockito.mock(Driver.class);
        Mockito.when(driver1.getId()).thenReturn(1L);
        Mockito.when(driver1.isActive()).thenReturn(true);

        Driver driver2 = Mockito.mock(Driver.class);
        Mockito.when(driver2.getId()).thenReturn(2L);
        Mockito.when(driver2.isActive()).thenReturn(true);

        Location endLocation1 = Mockito.mock(Location.class);
        Mockito.when(endLocation1.getLatitude()).thenReturn(45.0f);
        Mockito.when(endLocation1.getLongitude()).thenReturn(19.0f);
        Route route1 = Mockito.mock(Route.class);
        Mockito.when(route1.getEndPoint()).thenReturn(endLocation1);

        Ride currentRideOfDriver1 = Mockito.mock(Ride.class);
        Mockito.when(currentRideOfDriver1.getRoutes()).thenReturn(List.of(route1));
        Mockito.when(currentRideOfDriver1.getStatus()).thenReturn(Enums.RideStatus.ACTIVE);
        Mockito.when(currentRideOfDriver1.getStartTime()).thenReturn(LocalDateTime.now());
        Mockito.when(currentRideOfDriver1.getEstimatedTimeInMinutes()).thenReturn(33);
        Mockito.when(rideRepository.findByDriverId(driver1.getId())).thenReturn(List.of(currentRideOfDriver1));

        Location endLocation2 = Mockito.mock(Location.class);
        Mockito.when(endLocation2.getLatitude()).thenReturn(45.9f);
        Mockito.when(endLocation2.getLongitude()).thenReturn(19.9f);
        Route route2 = Mockito.mock(Route.class);
        Mockito.when(route2.getEndPoint()).thenReturn(endLocation2);

        Ride currentRideOfDriver2 = Mockito.mock(Ride.class);
        Mockito.when(currentRideOfDriver2.getRoutes()).thenReturn(List.of(route2));
        Mockito.when(currentRideOfDriver2.getStatus()).thenReturn(Enums.RideStatus.ACTIVE);
        Mockito.when(currentRideOfDriver2.getStartTime()).thenReturn(LocalDateTime.now());
        Mockito.when(currentRideOfDriver2.getEstimatedTimeInMinutes()).thenReturn(32);
        Mockito.when(rideRepository.findByDriverId(driver2.getId())).thenReturn(List.of(currentRideOfDriver2));

        Mockito.when(mapService
                        .getDistance(
                                departureLocation.getLatitude(),
                                departureLocation.getLongitude(),
                                driver1Location.getLatitude(),
                                driver1Location.getLongitude()))
                .thenReturn(100);
        Mockito.when(mapService
                        .getDistance(
                                departureLocation.getLatitude(),
                                departureLocation.getLongitude(),
                                driver2Location.getLatitude(),
                                driver2Location.getLongitude()))
                .thenReturn(50);

        Mockito.when(driverRepository.findAll()).thenReturn(List.of(driver1, driver2));

        Mockito.when(workHourService.hoursWorked(driver1.getId(), LocalDate.now())).thenReturn(5);
        Mockito.when(workHourService.hoursWorked(driver2.getId(), LocalDate.now())).thenReturn(5);

        Mockito.when(passengerRepository.findById(anyLong())).thenReturn(Optional.of(Mockito.mock(Passenger.class)));

        VehicleType vehicleType = Mockito.mock(VehicleType.class);
        Mockito.when(vehicleType.getPricePerKm()).thenReturn(120);
        Mockito.when(vehicleType.getName()).thenReturn("STANDARD");
        Mockito.when(vehicleTypeRepository.findByName("STANDARD")).thenReturn(Optional.of(vehicleType));

        LocationForRideDto locationForRideDto = Mockito.mock(LocationForRideDto.class);
        Mockito.when(locationForRideDto.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(locationForRideDto.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(locationForRideDto.getDeparture()).thenReturn(departureLocation);
        Mockito.when(locationForRideDto.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().plusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(locationForRideDto));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getVehicleType()).thenReturn("STANDARD");
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        RideDto rideDto = rideService.scheduleRide(rideCreationDto);
        String expectedStartTime = LocalDateTime
                .parse(rideCreationDto.getScheduledTime(), Constants.customDateTimeFormat)
                .plusMinutes(Constants.vehicleWaitTimeInMinutes)
                .format(Constants.customDateTimeFormat);
        assertEquals(expectedStartTime, rideDto.getStartTime());
        assertEquals(locationForRideDto.getEstimatedTimeInMinutes(), rideDto.getEstimatedTimeInMinutes());
        assertEquals(rideCreationDto.getVehicleType(), rideDto.getVehicleType());
        assertEquals(rideCreationDto.getBabyTransport(), rideDto.getBabyTransport());
        assertEquals(rideCreationDto.getPetTransport(), rideDto.getPetTransport());
        assertEquals(rideCreationDto.getPassengers().size(), rideDto.getPassengers().size());
        assertEquals(driver2.getId(), rideDto.getDriver().getId());
    }

    @Test
    public void scheduleRide_ShouldThrowDriverNotFoundExceptionIfThereAreNoActiveDrivers() {
        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.isActive()).thenReturn(false);

        Mockito.when(driverRepository.findAll()).thenReturn(List.of(driver));

        LocationForRideDto route = Mockito.mock(LocationForRideDto.class);
        Mockito.when(route.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(route.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(route.getDeparture()).thenReturn(Mockito.mock(GeoCoordinateDto.class));
        Mockito.when(route.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().plusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(route));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        assertThrows(DriverNotFoundException.class, () -> rideService.scheduleRide(rideCreationDto));
    }

    @Test
    public void scheduleRide_ShouldThrowDriverNotFoundExceptionIfAllDriversWorkedForMoreThanEightHours() {
        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.isActive()).thenReturn(true);

        Mockito.when(driverRepository.findAll()).thenReturn(List.of(driver));

        Mockito.when(workHourService.hoursWorked(driver.getId(), LocalDate.now())).thenReturn(10);

        LocationForRideDto route = Mockito.mock(LocationForRideDto.class);
        Mockito.when(route.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(route.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(route.getDeparture()).thenReturn(Mockito.mock(GeoCoordinateDto.class));
        Mockito.when(route.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().plusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(route));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        assertThrows(DriverNotFoundException.class, () -> rideService.scheduleRide(rideCreationDto));
    }
    @Test
    public void scheduleRide_ShouldThrowDriverNotFoundExceptionIfTheEstimatedEndTimeWillIntertwineWithAlreadyExistingRide() {
        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.isActive()).thenReturn(true);

        Mockito.when(driverRepository.findAll()).thenReturn(List.of(driver));

        Mockito.when(workHourService.hoursWorked(driver.getId(), LocalDate.now())).thenReturn(5);

        Ride scheduledRideOfDriver = Mockito.mock(Ride.class);
        Mockito.when(scheduledRideOfDriver.getStatus()).thenReturn(Enums.RideStatus.ACCEPTED);
        Mockito.when(scheduledRideOfDriver.getStartTime()).thenReturn(LocalDateTime.now().plusMinutes(35));
        Mockito.when(scheduledRideOfDriver.getEstimatedTimeInMinutes()).thenReturn(15);
        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(List.of(scheduledRideOfDriver));

        LocationForRideDto route = Mockito.mock(LocationForRideDto.class);
        Mockito.when(route.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(route.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(route.getDeparture()).thenReturn(Mockito.mock(GeoCoordinateDto.class));
        Mockito.when(route.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().plusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(route));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        assertThrows(DriverNotFoundException.class, () -> rideService.scheduleRide(rideCreationDto));
    }

    @Test
    public void scheduleRide_ShouldThrowDriverNotFoundExceptionIfAllDriversWillHaveActiveRidesAtThatMoment() {
        Driver driver = Mockito.mock(Driver.class);
        Mockito.when(driver.isActive()).thenReturn(true);

        Mockito.when(driverRepository.findAll()).thenReturn(List.of(driver));

        Mockito.when(workHourService.hoursWorked(driver.getId(), LocalDate.now())).thenReturn(5);

        Ride scheduledRideOfDriver = Mockito.mock(Ride.class);
        Mockito.when(scheduledRideOfDriver.getStatus()).thenReturn(Enums.RideStatus.ACCEPTED);
        Mockito.when(scheduledRideOfDriver.getStartTime()).thenReturn(LocalDateTime.now().plusMinutes(25));
        Mockito.when(scheduledRideOfDriver.getEstimatedTimeInMinutes()).thenReturn(10);
        Mockito.when(rideRepository.findByDriverId(driver.getId())).thenReturn(List.of(scheduledRideOfDriver));

        LocationForRideDto route = Mockito.mock(LocationForRideDto.class);
        Mockito.when(route.getEstimatedTimeInMinutes()).thenReturn(30);
        Mockito.when(route.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(route.getDeparture()).thenReturn(Mockito.mock(GeoCoordinateDto.class));
        Mockito.when(route.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().plusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(route));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        assertThrows(DriverNotFoundException.class, () -> rideService.scheduleRide(rideCreationDto));
    }

    @Test
    public void scheduleRide_ShouldThrowRideAlreadyPendingExceptionIfAnyPassengerHasPendingRides() {
        UserRefDto passenger1 = Mockito.mock(UserRefDto.class);
        Mockito.when(passenger1.getId()).thenReturn(1L);

        List<UserRefDto> passengers = List.of(passenger1);

        Ride pendingRide = Mockito.mock(Ride.class);
        Mockito.when(rideRepository.findByPassengersIdAndStatus(passenger1.getId(), Enums.RideStatus.PENDING.ordinal()))
                .thenReturn(List.of(pendingRide));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(passengers);

        assertThrows(RideAlreadyPendingException.class, () -> rideService.scheduleRide(rideCreationDto));
    }

    @Test
    public void scheduleRide_ShouldThrowSchedulingRideAtInvalidDateExceptionIfStartTimeIsInPast() {
        LocationForRideDto route = Mockito.mock(LocationForRideDto.class);
        Mockito.when(route.getEstimatedTimeInMinutes()).thenReturn(30);
        Mockito.when(route.getDistanceInMeters()).thenReturn(8000);
        Mockito.when(route.getDeparture()).thenReturn(Mockito.mock(GeoCoordinateDto.class));
        Mockito.when(route.getDestination()).thenReturn(Mockito.mock(GeoCoordinateDto.class));

        RideCreationDto rideCreationDto = Mockito.mock(RideCreationDto.class);
        Mockito.when(rideCreationDto.getScheduledTime()).thenReturn(LocalDateTime.now().minusMinutes(30).format(Constants.customDateTimeFormat));
        Mockito.when(rideCreationDto.getLocations()).thenReturn(List.of(route));
        Mockito.when(rideCreationDto.getPassengers()).thenReturn(List.of(Mockito.mock(UserRefDto.class)));
        Mockito.when(rideCreationDto.getBabyTransport()).thenReturn(false);
        Mockito.when(rideCreationDto.getPetTransport()).thenReturn(false);

        assertThrows(SchedulingRideAtInvalidDateException.class, () -> rideService.scheduleRide(rideCreationDto));
    }
}
