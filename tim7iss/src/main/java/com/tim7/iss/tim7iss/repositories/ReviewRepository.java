package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByRideId(Long rideId);

    @Query(
            value = "SELECT * FROM REVIEW WHERE vehicle_id = ?1",
            countQuery = "SELECT count(*) FROM REVIEW",
            nativeQuery = true)
    List<Review> findAllReviewsByVehiclesId(Long vehicleId);

    @Query(
            value = "SELECT * FROM REVIEW WHERE driver_id = ?1",
            countQuery = "SELECT count(*) FROM REVIEW",
            nativeQuery = true)
    List<Review> findAllReviewsByDriverId(Long driverId);


}
