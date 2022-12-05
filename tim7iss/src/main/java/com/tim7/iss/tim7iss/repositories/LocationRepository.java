package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    public Location findByYAndX(double latitude, double longitude);
}
