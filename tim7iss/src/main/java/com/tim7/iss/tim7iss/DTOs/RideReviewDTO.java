package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class RideReviewDTO {
    private Set<ReviewDTO> vehicleReviews = new HashSet<>();
    private Set<ReviewDTO> driverReviews = new HashSet<>();

    public RideReviewDTO(Set<Review> vehicleReviews, Set<Review> driverReviews) {
        for (Review review : vehicleReviews) {
            ReviewDTO vehicleReviewDTO = new ReviewDTO(review);
            this.vehicleReviews.add(vehicleReviewDTO);
        }

        for (Review review : driverReviews) {
            ReviewDTO driverReviewDTO = new ReviewDTO(review);
            this.driverReviews.add(driverReviewDTO);
        }

    }
}
