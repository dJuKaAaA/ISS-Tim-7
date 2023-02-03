package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByDriverId(Long driverId);

    List<Ride> findRidesByPassengersId(Long passengerId);

    @Query(value = "select r.* from ride r left join passenger_rides fr on r.id = fr.ride_id where fr.passenger_id = ?1 and r.status = ?2", nativeQuery = true)
    List<Ride> findByPassengersIdAndStatus(Long passenger_id, Integer status);

    @Query(value = "select * from ride where driver_id = ?1 and status = ?2", nativeQuery = true)
    List<Ride> findByDriverIdAndStatus(Long driver_id, Integer status);

    Long countByPassengersId(Long passengerId);

}

