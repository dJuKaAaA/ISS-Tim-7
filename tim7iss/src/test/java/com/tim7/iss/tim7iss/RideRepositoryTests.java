package com.tim7.iss.tim7iss;

import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
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


    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private RoutesRepository routesRepository;



    @Test
    public void findRidesByDriverId_shouldReturnListOfRidesForGivenDriverId() {
        int numberOfExpectedRides = 2;
        Long driverId = 1L;
        List<Ride> rides = rideRepository.findRidesByDriverId(1L);

        assertEquals(numberOfExpectedRides, rides.size());
        assertEquals(driverId, rides.get(1).getDriver().getId());
        assertEquals(driverId, rides.get(1).getDriver().getId());
    }

    @Test
    public void findRidesByDriverId_shouldReturnEmptyListForGivenDriverId() {
        Long driverIdWithNoRides = 2L;
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

        Ride ride = new Ride(null, 1000, LocalDateTime.now().plusMinutes(20), null, route.getEstimatedTimeInMinutes()
                , false, driver.getVehicle().isPetsAllowed(), false, Enums.RideStatus.ACCEPTED, driver, driver.getVehicle().getVehicleType(), Set.of(passenger), null, List.of(route));

        Ride savedRide = rideRepository.save(ride);

        assertThat(savedRide).usingRecursiveComparison().ignoringFields("id").isEqualTo(ride);
    }


    @Test
    public void countByPassengersId_ShouldReturnNumberOfRidesForPassengerId() {
        Long passengerId = 5L;
        Long numberOfRides = 1L;
        Long retrievedNumberOfRides = rideRepository.countByPassengersId(passengerId);

        assertEquals(numberOfRides, retrievedNumberOfRides);
    }

    @Test
    public void countByPassengersId_ShouldReturnZeroNumberOfRidesForPassengerId() {
        Long passengerId = 7L;
        Long numberOfRides = 0L;
        Long retrievedNumberOfRides = rideRepository.countByPassengersId(passengerId);

        assertEquals(numberOfRides, retrievedNumberOfRides);
    }

    @Test
    public void countByPassengersId_ShouldReturnZeroNumberOfRideForNonExistingPassengerId(){
        Long passengerId = 132135L;
        Long numberOfRides = 0L;
        Long retrievedNumberOfRides = rideRepository.countByPassengersId(passengerId);

        assertEquals(numberOfRides, retrievedNumberOfRides);
    }


}
