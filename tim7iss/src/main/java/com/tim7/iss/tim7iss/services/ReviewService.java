package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.DTOs.POSTReviewDTO;
import com.tim7.iss.tim7iss.DTOs.ReviewDTO;
import com.tim7.iss.tim7iss.DTOs.ReviewsDTO;
import com.tim7.iss.tim7iss.DTOs.RideReviewDTO;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

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

    public ReviewDTO addVehicleReview(Long vehicleId, POSTReviewDTO postReviewDTO) {

        VehicleReview review = new VehicleReview();
        Vehicle vehicle = vehicleRepository.findById(vehicleId).get();
        review.setComment(postReviewDTO.getComment());
        review.setRating(postReviewDTO.getRating());
        review.setRide(null);
        review.setPassenger(null);
        review.setVehicle(vehicle);
//        review.setRide();
//        review.setPassenger();
        return new ReviewDTO(review);
    }

    public ReviewsDTO getVehicleReviews(Long vehicleId) {
        List<VehicleReview> vehicleReviews = vehicleReviewRepository.findAllByVehicleId(vehicleId);
        return new ReviewsDTO(new HashSet<>(vehicleReviews));
    }

    public ReviewDTO addDriverReview(Long driverId, POSTReviewDTO postReviewDTO) {
        DriverReview review = new DriverReview();
        Driver driver = driverRepository.findById(driverId).get();
        review.setComment(postReviewDTO.getComment());
        review.setRating(postReviewDTO.getRating());
        review.setRide(null);
        review.setPassenger(null);
        review.setDriver(driver);
//        review.setRide();
//        review.setPassenger();
        return new ReviewDTO(review);

    }

    public ReviewsDTO getDriverReviews(Long driveId) {
        List<DriverReview> driverReviews = driverReviewRepository.findAllByDriverId(driveId);
        return new ReviewsDTO(new HashSet<>(driverReviews));
    }

    public RideReviewDTO getRideReviews(Long rideId) {
        List<Review> reviews = reviewRepository.findAllByRideId(rideId);

        HashSet<Review> vehiclesReviews = new HashSet<>();
        HashSet<Review> driveReviews = new HashSet<>();

        for (Review review : reviews) {
            if (review instanceof VehicleReview) {
                vehiclesReviews.add(review);
            } else if (review instanceof DriverReview) {
                driveReviews.add(review);
            }
        }
        return new RideReviewDTO(vehiclesReviews, driveReviews);
    }
}
