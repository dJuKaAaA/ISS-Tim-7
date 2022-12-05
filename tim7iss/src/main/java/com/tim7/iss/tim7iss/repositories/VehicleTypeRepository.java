package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    Optional<VehicleType> findByName(String name);

}
