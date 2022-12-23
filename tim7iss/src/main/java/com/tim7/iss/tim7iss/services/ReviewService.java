package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.ReviewDto;
import com.tim7.iss.tim7iss.dto.RideReviewDto;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewService {
    private final Logger LOGGER = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleReviewRepository vehicleReviewRepository;

    @Autowired
    DriverReviewRepository driverReviewRepository;

    @Autowired
    PassengerRepository passengerRepository;

    public ResponseEntity<ReviewDto> addVehicleReview(Long vehicleId, Long rideId, ReviewDto postReviewDto) {

        VehicleReview review = new VehicleReview();
        Ride ride = rideRepository.findById(rideId).orElse(null);
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        Passenger passenger = passengerRepository.findById(3L).orElse(null); // TODO promeniti
        review.setComment(postReviewDto.getComment());
        review.setRating(postReviewDto.getRating());
        review.setRide(ride);
        review.setPassenger(passenger);
        review.setVehicle(vehicle);
        reviewRepository.save(review);
        return new ResponseEntity<>(new ReviewDto(review), HttpStatus.OK);
    }

    public ResponseEntity<PaginatedResponseDto<ReviewDto>> getVehicleReviews(Long vehicleId) {
        Collection<ReviewDto> vehicleReviews = new ArrayList<>();
        vehicleReviewRepository.findAllByVehicleId(vehicleId).forEach(review -> vehicleReviews.add(new ReviewDto(review)));
        return new ResponseEntity<>(
                new PaginatedResponseDto<>(vehicleReviews.size(), vehicleReviews), HttpStatus.OK);
    }

    public ResponseEntity<ReviewDto> addDriverReview(Long driverId, Long rideId, ReviewDto postReviewDto) {
        DriverReview review = new DriverReview();
        Driver driver = driverRepository.findById(driverId).orElse(null);
        Ride ride = rideRepository.findById(rideId).orElse(null);
        Passenger passenger = passengerRepository.findById(3L).orElse(null); // TODO promeniti

        review.setComment(postReviewDto.getComment());
        review.setRating(postReviewDto.getRating());
        review.setRide(ride);
        review.setPassenger(passenger);
        review.setDriver(driver);
        driverReviewRepository.save(review);
        return new ResponseEntity<>(new ReviewDto(review), HttpStatus.OK);

    }

    public ResponseEntity<PaginatedResponseDto<ReviewDto>> getDriverReviews(Long driverId) {
        Collection<ReviewDto> driverReviews = new ArrayList<>();
        driverReviewRepository.findAllByDriverId(driverId).forEach(review -> driverReviews.add(new ReviewDto(review)));
        return new ResponseEntity<>(
                new PaginatedResponseDto<>(driverReviews.size(), driverReviews), HttpStatus.OK);
    }

    public ResponseEntity<Collection<RideReviewDto>> getRideReviews(Long rideId) {
        List<Review> reviews = reviewRepository.findAllByRideId(rideId);

        // what if passenger leaves review for driver or vehicle but leaves out review for the other?

        Set<Long> passengerIds = new HashSet<>();
        Map<Long, ReviewDto> vehicleReviews = new HashMap<>();
        Map<Long, ReviewDto> driveReviews = new HashMap<>();

        for (Review review : reviews) {
            if (review instanceof VehicleReview) {
                vehicleReviews.put(review.getPassenger().getId(), new ReviewDto(review));
            } else if (review instanceof DriverReview) {
                driveReviews.put(review.getPassenger().getId(), new ReviewDto(review));
            }
            passengerIds.add(review.getPassenger().getId());
        }
        Collection<RideReviewDto> rideReviews = new ArrayList<>();

        for (Long passengerId : passengerIds) {
            rideReviews.add(new RideReviewDto(vehicleReviews.get(passengerId), driveReviews.get(passengerId)));
        }

        return new ResponseEntity<>(rideReviews, HttpStatus.OK);
    }
}
