package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutesRepository extends JpaRepository <Route, Long> {
    Route findByStartingPointIdAndEndPointId(Long startingPointId, Long endPointId);
}
