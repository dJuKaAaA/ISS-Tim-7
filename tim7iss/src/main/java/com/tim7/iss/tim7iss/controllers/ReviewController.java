package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.ReviewDto;
import com.tim7.iss.tim7iss.dto.RideReviewDto;
import com.tim7.iss.tim7iss.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Transactional
public class ReviewController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    ReviewService reviewService;

    @PostMapping("/api/review/{rideId}/vehicle/{id}")
    public ResponseEntity<ReviewDto> addVehicleReview(@PathVariable("rideId") Long rideId,
                                                      @PathVariable("id") Long vehicleId,
                                                      @RequestBody ReviewDto postReviewDto) {
        LOGGER.info("add vehicle review");
        return reviewService.addVehicleReview(vehicleId, rideId, postReviewDto);
//        return new ResponseEntity<>(reviewService.addVehicleReviewK1(), HttpStatus.OK);
    }

    @GetMapping("/api/review/vehicle/{id}")
    public ResponseEntity<PaginatedResponseDto<ReviewDto>> getVehicleReviews(@PathVariable("id") Long vehicleId) {
        LOGGER.info("get vehicle reviews");
        return reviewService.getVehicleReviews(vehicleId);
//        return new ResponseEntity<>(reviewService.getVehicleReviewsK1(), HttpStatus.OK);
    }

    @PostMapping("/api/review/{rideId}/driver/{id}")
    public ResponseEntity<ReviewDto> addDriverReview(@PathVariable("rideId") Long rideId,
                                                     @PathVariable("id") Long driverId,
                                                     @RequestBody ReviewDto postReviewDto) {
        LOGGER.info("add driver review");
        return reviewService.addDriverReview(driverId,rideId, postReviewDto);
//        return new ResponseEntity<>(reviewService.addDriverReviewK1(), HttpStatus.OK);
    }

    //
    @GetMapping("/api/review/driver/{id}")
    public ResponseEntity<PaginatedResponseDto<ReviewDto>> getDriverReviews(@PathVariable("id") Long driveId) {
        LOGGER.info("get driver reviews");
        return reviewService.getDriverReviews(driveId);
//        return new ResponseEntity<>(reviewService.getDriverReviewsK1(), HttpStatus.OK);
    }

    @GetMapping("/api/review/{rideId}")
    public ResponseEntity<Collection<RideReviewDto>> getRideReviews(@PathVariable("rideId") Long rideId) {
        LOGGER.info("get ride reviews");
        return reviewService.getRideReviews(rideId);
//        return new ResponseEntity<>(reviewService.getRideReviewsK1(), HttpStatus.OK);
    }

}