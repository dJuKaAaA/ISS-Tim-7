package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Vehicle findByDriverId(Long driverId);

}
