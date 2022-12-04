package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.POSTReviewDTO;
import com.tim7.iss.tim7iss.DTOs.ReviewDTO;
import com.tim7.iss.tim7iss.DTOs.ReviewsDTO;
import com.tim7.iss.tim7iss.DTOs.RideReviewDTO;
import com.tim7.iss.tim7iss.models.Review;
import com.tim7.iss.tim7iss.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
public class ReviewController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    ReviewService reviewService;

    @PostMapping("/api/review/vehicle/{id}")
    public ReviewDTO addVehicleReview(@PathVariable("id") Long vehicleId, @RequestBody POSTReviewDTO postReviewDTO) {
        LOGGER.info("add vehicle review");
        return reviewService.addVehicleReview(vehicleId,postReviewDTO);
    }

    @GetMapping("/api/review/vehicle/{id}")
    public ReviewsDTO getVehicleReviews(@PathVariable("id") Long vehicleId) {
        LOGGER.info("get vehicle reviews");
        return reviewService.getVehicleReviews(vehicleId);
    }

    @PostMapping("/api/review/driver/{id}")
    public ReviewDTO addDriverReview(@PathVariable("id") Long driverId, @RequestBody POSTReviewDTO postReviewDTO) {
        LOGGER.info("add driver review");
        return reviewService.addDriverReview(driverId, postReviewDTO);
    }

    //
    @GetMapping("/api/review/driver/{id}")
    public ReviewsDTO getDriverReviews(@PathVariable("id") Long driveId) {
        LOGGER.info("get driver reviews");
        return reviewService.getDriverReviews(driveId);
    }

    @GetMapping("/api/review/{rideId}")
    public RideReviewDTO getRideReviews(@PathVariable("rideId") Long rideId) {
        LOGGER.info("get ride reviews");
        return reviewService.getRideReviews(rideId);
    }

}