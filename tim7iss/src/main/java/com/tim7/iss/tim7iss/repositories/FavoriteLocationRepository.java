package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.FavoriteLocation;
import com.tim7.iss.tim7iss.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteLocationRepository extends JpaRepository<FavoriteLocation, Long> {
    Optional<List<FavoriteLocation>> findBySubmittedById(Long id);
}
