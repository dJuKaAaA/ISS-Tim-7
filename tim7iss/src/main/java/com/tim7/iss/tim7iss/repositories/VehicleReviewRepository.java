package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.VehicleReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleReviewRepository extends JpaRepository<VehicleReview,Long> {

    List<VehicleReview> findAllByVehicleId(Long vehicleId);

    Long countByVehicleId(Long vehicleId);

}
