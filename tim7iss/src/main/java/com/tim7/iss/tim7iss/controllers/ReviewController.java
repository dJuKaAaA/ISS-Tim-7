package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.ReviewDto;
import com.tim7.iss.tim7iss.dto.RideReviewDto;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
import com.tim7.iss.tim7iss.exceptions.VehicleNotFoundException;
import com.tim7.iss.tim7iss.services.ReviewService;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Transactional
@Validated
public class ReviewController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    ReviewService reviewService;

    @Autowired
    TokenUtils tokenUtils;

    @PostMapping("/api/review/{rideId}/vehicle")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ReviewDto> addVehicleReview(@RequestHeader(value = "Authorization") String authHeader, @PathVariable("rideId") Long rideId, @Valid @RequestBody ReviewDto reviewDto) throws RideNotFoundException {

        LOGGER.info("add vehicle review");
        String token = tokenUtils.getToken(authHeader);
        String email = tokenUtils.getEmailFromToken(token);
        return reviewService.addVehicleReview(rideId, email, reviewDto);

    }

    @GetMapping("/api/review/vehicle/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity<PaginatedResponseDto<ReviewDto>> getVehicleReviews(@PathVariable("id") Long vehicleId) throws VehicleNotFoundException {
        LOGGER.info("get vehicle reviews");
        return reviewService.getVehicleReviews(vehicleId);
    }

    @PostMapping("/api/review/{rideId}/driver")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ReviewDto> addDriverReview(@RequestHeader(value = "Authorization") String authHeader, @PathVariable("rideId") Long rideId, @Valid @RequestBody ReviewDto reviewDto) throws RideNotFoundException {
        LOGGER.info("add driver review");
        String token = tokenUtils.getToken(authHeader);
        String email = tokenUtils.getEmailFromToken(token);
        return reviewService.addDriverReview(rideId, email, reviewDto);
    }

    @GetMapping("/api/review/driver/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity<PaginatedResponseDto<ReviewDto>> getDriverReviews(@PathVariable("id") Long driveId) throws RideNotFoundException, DriverNotFoundException {
        LOGGER.info("get driver reviews");
        return reviewService.getDriverReviews(driveId);
    }

    @GetMapping("/api/review/{rideId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity<Collection<RideReviewDto>> getRideReviews(@PathVariable("rideId") Long rideId) throws RideNotFoundException {
        LOGGER.info("get ride reviews");
        return reviewService.getRideReviews(rideId);
    }

}