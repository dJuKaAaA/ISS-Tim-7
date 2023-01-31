package com.tim7.iss.tim7iss;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.PassengerRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.repositories.RoutesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideRepositoryTests {

    public CustomTestData customTestData = new CustomTestData();

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private RoutesRepository routesRepository;


    @Test
    public void findRidesByDriverId_shouldReturnListOfRidesForGivenDriverId() throws IOException {
        Long driverId = customTestData.getDriver().getId();
        int numberOfExpectedRides = 5;
        List<Ride> rides = rideRepository.findRidesByDriverId(driverId);
        assertEquals(numberOfExpectedRides, rides.size());

        for (Ride ride : rides) {
            assertEquals(driverId, ride.getDriver().getId());
            assertEquals(driverId, ride.getDriver().getId());

        }
    }

    @Test
    public void findRidesByDriverId_shouldReturnEmptyListForGivenDriverId() throws IOException {
        Long driverIdWithNoRides = customTestData.getDriverWithNoRides().getId();
        int numberOfExpectedRides = 0;
        List<Ride> rides = rideRepository.findRidesByDriverId(driverIdWithNoRides);
        assertEquals(numberOfExpectedRides, rides.size());
    }

    @Test
    public void findRidesByDriverId_shouldReturnEmptyListForNonExistingDriverId() {
        Long nonExistingDriverId = 123123L;
        List<Ride> rides = rideRepository.findRidesByDriverId(nonExistingDriverId);
        assertTrue(rides.isEmpty());

    }

    @Test
    public void shouldSaveRide() {

        // Učitaj driver iz baze podataka
        Driver driver = driverRepository.findById(1L).get();

        // Učitaj passenger iz baze podataka
        Passenger passenger = passengerRepository.findById(3L).get();

        // Učitaj route iz baze podataka
        Route route = routesRepository.findById(1L).get();

        Ride ride = new Ride(null, 1000, LocalDateTime.now().plusMinutes(20), null, route.getEstimatedTimeInMinutes(), false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        Ride savedRide = rideRepository.save(ride);

        assertThat(savedRide).usingRecursiveComparison().ignoringFields("id").isEqualTo(ride);
    }
    
    @Test
    public void countByPassengersId_ShouldReturnNumberOfRidesForPassengerId() throws IOException {
        Long passengerId = customTestData.getPassenger1().getId();
        Long numberOfRides = 5L;
        Long retrievedNumberOfRides = rideRepository.countByPassengersId(passengerId);

        assertEquals(numberOfRides, retrievedNumberOfRides);
    }

    @Test
    public void countByPassengersId_ShouldReturnZeroNumberOfRidesForPassengerId() throws IOException {
        Long passengerId = customTestData.getPassengerWithNoRides().getId();
        Long numberOfRides = 0L;
        Long retrievedNumberOfRides = rideRepository.countByPassengersId(passengerId);

        assertEquals(numberOfRides, retrievedNumberOfRides);
    }

    @Test
    public void countByPassengersId_ShouldReturnZeroNumberOfRideForNonExistingPassengerId() {
        Long passengerId = 132135L;
        Long numberOfRides = 0L;
        Long retrievedNumberOfRides = rideRepository.countByPassengersId(passengerId);

        assertEquals(numberOfRides, retrievedNumberOfRides);
    }


}
