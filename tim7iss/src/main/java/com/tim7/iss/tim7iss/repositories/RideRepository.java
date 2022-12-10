package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Ride;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findRidesByPassengersId(Long passengerId);

    List<Ride> findRidesByDriverId(Long driverId);

    List<Ride> findByDriverId(Long driverId, Pageable page);
}

