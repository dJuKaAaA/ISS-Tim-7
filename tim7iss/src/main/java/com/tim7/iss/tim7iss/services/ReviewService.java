package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.ReviewDto;
import com.tim7.iss.tim7iss.dto.RideReviewDto;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
import com.tim7.iss.tim7iss.exceptions.VehicleNotFoundException;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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

    public ResponseEntity<ReviewDto> addVehicleReview(Long rideId, String email, @Valid ReviewDto createReviewDto) throws RideNotFoundException {

        VehicleReview review = new VehicleReview();
        Ride ride = rideRepository.findById(rideId).orElseThrow(RideNotFoundException::new);
        Vehicle vehicle = ride.getDriver().getVehicle();
        Passenger passenger = passengerRepository.findByEmailAddress(email).get();

        review.setComment(createReviewDto.getComment());
        review.setRating(createReviewDto.getRating());
        review.setRide(ride);
        review.setPassenger(passenger);
        review.setVehicle(vehicle);

        reviewRepository.save(review);
        return new ResponseEntity<>(new ReviewDto(review), HttpStatus.OK);
    }

    public ResponseEntity<PaginatedResponseDto<ReviewDto>> getVehicleReviews(Long vehicleId) throws VehicleNotFoundException {
        Collection<ReviewDto> vehicleReviews = new ArrayList<>();
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(VehicleNotFoundException::new);
        vehicleReviewRepository.findAllByVehicleId(vehicle.getId()).forEach(review -> vehicleReviews.add(new ReviewDto(review)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(vehicleReviews.size(), vehicleReviews), HttpStatus.OK);
    }

    public ResponseEntity<ReviewDto> addDriverReview(Long rideId, String email, @Valid ReviewDto createReviewDto) throws RideNotFoundException {
        DriverReview review = new DriverReview();

        Ride ride = rideRepository.findById(rideId).orElseThrow(RideNotFoundException::new);
        Driver driver = ride.getDriver();
        Passenger passenger = passengerRepository.findByEmailAddress(email).get();

        review.setComment(createReviewDto.getComment());
        review.setRating(createReviewDto.getRating());
        review.setRide(ride);
        review.setPassenger(passenger);
        review.setDriver(driver);
        driverReviewRepository.save(review);

        return new ResponseEntity<>(new ReviewDto(review), HttpStatus.OK);

    }

    public ResponseEntity<PaginatedResponseDto<ReviewDto>> getDriverReviews(Long driverId) throws RideNotFoundException, DriverNotFoundException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);
        Collection<ReviewDto> driverReviews = new ArrayList<>();
        driverReviewRepository.findAllByDriverId(driver.getId()).forEach(review -> driverReviews.add(new ReviewDto(review)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(driverReviews.size(), driverReviews), HttpStatus.OK);
    }

    public ResponseEntity<Collection<RideReviewDto>> getRideReviews(Long rideId) throws RideNotFoundException {
        Ride ride = rideRepository.findById(rideId).orElseThrow(RideNotFoundException::new);
        List<Review> reviews = reviewRepository.findAllByRideId(ride.getId());

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
