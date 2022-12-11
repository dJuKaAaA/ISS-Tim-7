package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.POSTReviewDTO;
import com.tim7.iss.tim7iss.DTOs.ReviewDTO;
import com.tim7.iss.tim7iss.DTOs.ReviewsDTO;
import com.tim7.iss.tim7iss.DTOs.RideReviewDTO;
import com.tim7.iss.tim7iss.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class ReviewController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    ReviewService reviewService;

    @PostMapping("/api/review/{rideId}/vehicle/{id}")
    public ResponseEntity<ReviewDTO> addVehicleReview(@PathVariable("rideId") Long rideId,
                                                      @PathVariable("id") Long vehicleId,
                                                      @RequestBody POSTReviewDTO postReviewDTO) {
        LOGGER.info("add vehicle review");
//        return reviewService.addVehicleReview(vehicleId, rideId, postReviewDTO);
        return new ResponseEntity<>(reviewService.addVehicleReviewK1(), HttpStatus.OK);
    }

    @GetMapping("/api/review/vehicle/{id}")
    public ResponseEntity<ReviewsDTO> getVehicleReviews(@PathVariable("id") Long vehicleId) {
        LOGGER.info("get vehicle reviews");
//        return reviewService.getVehicleReviews(vehicleId);
        return new ResponseEntity<>(reviewService.getVehicleReviewsK1(), HttpStatus.OK);
    }

    @PostMapping("/api/review/{rideId}/driver/{id}")
    public ResponseEntity<ReviewDTO> addDriverReview(@PathVariable("rideId") Long rideId,
                                                     @PathVariable("id") Long driverId,
                                                     @RequestBody POSTReviewDTO postReviewDTO) {
        LOGGER.info("add driver review");
//        return reviewService.addDriverReview(driverId,rideId, postReviewDTO);
        return new ResponseEntity<>(reviewService.addDriverReviewK1(), HttpStatus.OK);
    }

    //
    @GetMapping("/api/review/driver/{id}")
    public ResponseEntity<ReviewsDTO> getDriverReviews(@PathVariable("id") Long driveId) {
        LOGGER.info("get driver reviews");
//        return reviewService.getDriverReviews(driveId);
        return new ResponseEntity<>(reviewService.getDriverReviewsK1(), HttpStatus.OK);
    }

    @GetMapping("/api/review/{rideId}")
    public ResponseEntity<RideReviewDTO> getRideReviews(@PathVariable("rideId") Long rideId) {
        LOGGER.info("get ride reviews");
//        return reviewService.getRideReviews(rideId);
        return new ResponseEntity<>(reviewService.getRideReviewsK1(), HttpStatus.OK);
    }

}