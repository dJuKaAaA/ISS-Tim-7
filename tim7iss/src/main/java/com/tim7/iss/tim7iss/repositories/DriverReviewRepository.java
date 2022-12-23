package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.DriverReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverReviewRepository extends JpaRepository<DriverReview,Long> {

    List<DriverReview> findAllByDriverId(Long driverId);

    Long countByDriverId(Long driverId);

}
