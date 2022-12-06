package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.DTOs.*;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public ReviewDTO addVehicleReview(Long vehicleId, Long rideId, POSTReviewDTO postReviewDTO) {

        VehicleReview review = new VehicleReview();
        Ride ride = rideRepository.findById(rideId).get();
        Vehicle vehicle = vehicleRepository.findById(vehicleId).get();
        review.setComment(postReviewDTO.getComment());
        review.setRating(postReviewDTO.getRating());
        review.setRide(ride);
        review.setPassenger(null);
        review.setVehicle(vehicle);
        return new ReviewDTO(review);
    }

    public ReviewDTO addVehicleReviewK1() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setRating(3);
        reviewDTO.setComment("comment");
        SimplePassengerDTO simplePassengerDTO = new SimplePassengerDTO(1L, "email");
        reviewDTO.setPassenger(simplePassengerDTO);
        return reviewDTO;
    }

    public ReviewsDTO getVehicleReviews(Long vehicleId) {
        List<VehicleReview> vehicleReviews = vehicleReviewRepository.findAllByVehicleId(vehicleId);
        return new ReviewsDTO(new HashSet<>(vehicleReviews));
    }

    public ReviewsDTO getVehicleReviewsK1() {

        ReviewsDTO reviewsDTO = new ReviewsDTO();
        reviewsDTO.setTotalCount(1);
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setRating(3);
        reviewDTO.setComment("comment");

        SimplePassengerDTO simplePassengerDTO = new SimplePassengerDTO(1L, "email");
        reviewDTO.setPassenger(simplePassengerDTO);

        Set<ReviewDTO> reviewDTOSet = new HashSet<>();
        reviewDTOSet.add(reviewDTO);
        reviewsDTO.setResults(reviewDTOSet);

        return reviewsDTO;
    }

    public ReviewDTO addDriverReview(Long driverId, Long rideId, POSTReviewDTO postReviewDTO) {
        DriverReview review = new DriverReview();
        Driver driver = driverRepository.findById(driverId).get();
        Ride ride = rideRepository.findById(rideId).get();

        review.setComment(postReviewDTO.getComment());
        review.setRating(postReviewDTO.getRating());
        review.setRide(ride);
        review.setPassenger(null);
        review.setDriver(driver);
        return new ReviewDTO(review);

    }

    public ReviewDTO addDriverReviewK1() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setRating(3);
        reviewDTO.setComment("comment");
        SimplePassengerDTO simplePassengerDTO = new SimplePassengerDTO(1L, "email");
        reviewDTO.setPassenger(simplePassengerDTO);
        return reviewDTO;
    }

    public ReviewsDTO getDriverReviews(Long driveId) {
        List<DriverReview> driverReviews = driverReviewRepository.findAllByDriverId(driveId);
        return new ReviewsDTO(new HashSet<>(driverReviews));
    }

    public ReviewsDTO getDriverReviewsK1() {
        ReviewsDTO reviewsDTO = new ReviewsDTO();
        reviewsDTO.setTotalCount(1);
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setRating(3);
        reviewDTO.setComment("comment");

        SimplePassengerDTO simplePassengerDTO = new SimplePassengerDTO(1L, "email");
        reviewDTO.setPassenger(simplePassengerDTO);

        Set<ReviewDTO> reviewDTOSet = new HashSet<>();
        reviewDTOSet.add(reviewDTO);
        reviewsDTO.setResults(reviewDTOSet);

        return reviewsDTO;
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

    public RideReviewDTO getRideReviewsK1() {


        ReviewDTO vehicleReviewDTO = new ReviewDTO();
        vehicleReviewDTO.setId(1L);
        vehicleReviewDTO.setRating(3);
        vehicleReviewDTO.setComment("comment");

        SimplePassengerDTO simplePassengerDTO = new SimplePassengerDTO(1L, "email");
        vehicleReviewDTO.setPassenger(simplePassengerDTO);

        Set<ReviewDTO> vehicleReviews = new HashSet<>();
        vehicleReviews.add(vehicleReviewDTO);


        ReviewDTO driverReviewDTO = new ReviewDTO();
        driverReviewDTO.setId(1L);
        driverReviewDTO.setRating(3);
        driverReviewDTO.setComment("comment");

        SimplePassengerDTO simplePassengerDTO2 = new SimplePassengerDTO(1L, "email");
        driverReviewDTO.setPassenger(simplePassengerDTO2);

        Set<ReviewDTO> driverReviews = new HashSet<>();
        driverReviews.add(driverReviewDTO);

        RideReviewDTO rideReviewDTO = new RideReviewDTO();
        rideReviewDTO.setVehicleReviews(vehicleReviews);
        rideReviewDTO.setDriverReviews(driverReviews);
        return rideReviewDTO;
    }
}
