package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.FavoriteLocation;
import com.tim7.iss.tim7iss.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteLocationRepository extends JpaRepository<FavoriteLocation, Long> {
}
