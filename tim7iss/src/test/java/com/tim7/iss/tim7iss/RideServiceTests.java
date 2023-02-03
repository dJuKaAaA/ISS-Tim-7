package com.tim7.iss.tim7iss;


import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import com.tim7.iss.tim7iss.services.DriverService;
import com.tim7.iss.tim7iss.services.FavoriteLocationService;
import com.tim7.iss.tim7iss.services.RideService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;

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
        Mockito.when(rideRepository.findByDriverId(Mockito.anyLong())).thenReturn(List.of(ride));


        PaginatedResponseDto expectedPaginatedResponseDto = new PaginatedResponseDto(List.of(rideDto).size(), List.of(rideDto));

        PaginatedResponseDto actualPaginatedResponseDto = rideService.getPaginatedRidesForDriverAsDto(Mockito.anyLong(), null);

        assertEquals(expectedPaginatedResponseDto, actualPaginatedResponseDto);
    }

    @Test
    public void getPaginatedRidesForDriverAsDto_shouldReturnPaginatedRidesWhenDriverHasNoRides() throws IOException, DriverNotFoundException {
        Driver driver = customTestData.getDriver();


        Mockito.when(driverRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(driver));
        Mockito.when(rideRepository.findByDriverId(Mockito.anyLong())).thenReturn(new ArrayList<>());


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
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenOnlyHavePendingRides() throws IOException, DriverNotFoundException {
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
    public void getScheduledRidesForDriverAsDto_shouldReturnScheduledRidesForDriverWhenHavePendingAndAcceptedRides() throws IOException, DriverNotFoundException {
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
    public void getScheduledRidesForDriverAsDto_shouldReturnEmptyListWhenDriverNotHaveScheduledRides() throws IOException, DriverNotFoundException {
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
    public void AnyRidesArePending_ShouldReturnTrueIfAnyPassengerHasPendingRides() {
        UserRefDto passenger1 = Mockito.mock(UserRefDto.class);
        Mockito.when(passenger1.getId()).thenReturn(1L);

        List<UserRefDto> passengers = List.of(passenger1);

        Ride pendingRide = Mockito.mock(Ride.class);

        Mockito.when(rideRepository.findByPassengersIdAndStatus(passenger1.getId(), Enums.RideStatus.PENDING.ordinal()))
                .thenReturn(List.of(pendingRide));
        assertEquals(true, rideService.AnyRidesArePending(passengers));
    }

    @Test
    public void AnyRidesArePending_ShouldReturnFalseIfNoPassengersHaveNoPendingRides() throws IOException {
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
    public void checkIfMoreThan9FavoriteLocations_ShouldReturnTrueIfPassengerHasMoreThanNineFavoriteLocations() {
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
    public void checkIfMoreThan9FavoriteLocations_ShouldReturnFalseIfPassengerHasLessThanNineFavoriteLocations() {
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
    public void createFavoriteLocation_ThrowsTooManyFavoriteLocationException() {
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
}
