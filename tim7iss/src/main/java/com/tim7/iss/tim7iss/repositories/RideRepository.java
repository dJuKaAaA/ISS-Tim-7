package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Enums;
import com.tim7.iss.tim7iss.models.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findRidesByDriverId(Long driverId);


    List<Ride> findRidesByPassengersId(Long passengerId);

    @Query(value = "select r.* from ride r left join passenger_rides fr on r.id = fr.ride_id where fr.passenger_id = ?1 and r.status = ?2", nativeQuery = true)
    Optional<List<Ride>> findByPassengersIdAndStatus(Long passenger_id, Integer status);

    @Query(value = "select * from ride where driver_id = ?1 and status = ?2", nativeQuery = true)
    Optional<List<Ride>> findByDriverIdAndStatus(Long driver_id, Integer status);

    Long countByPassengersId(Long passengerId);

}

