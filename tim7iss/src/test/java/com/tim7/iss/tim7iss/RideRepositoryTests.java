package com.tim7.iss.tim7iss;

import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideRepositoryTests {


    @Autowired
    private RideRepository rideRepository;


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
    public void findRidesByDriverId_shouldReturnNullForNonExistingDriverId() {
        Long nonExistingDriverId = 123123L;
        List<Ride> rides = rideRepository.findRidesByDriverId(nonExistingDriverId);
        assertTrue(rides.isEmpty());

    }
}
