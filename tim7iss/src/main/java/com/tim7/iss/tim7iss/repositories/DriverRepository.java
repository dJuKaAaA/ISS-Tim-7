package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByVehicleId(Long vehicleId);
}
