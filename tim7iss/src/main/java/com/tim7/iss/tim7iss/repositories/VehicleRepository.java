package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByDriverId(Long driverId);

    @Modifying
    @Query(value = "Update vehicle set location_id = ?1 where id = ?2", nativeQuery = true)
    void updateLocation(Long location_id, Long id);

}
