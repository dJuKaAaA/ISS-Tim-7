package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByLongitudeAndLatitude(double longitude, double latitude);

}
