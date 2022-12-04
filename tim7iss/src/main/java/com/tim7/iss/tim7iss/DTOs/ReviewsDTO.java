package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsDTO {
    private int totalCount;
    private Set<ReviewDTO> results = new HashSet<>();

    public ReviewsDTO(Set<Review> reviews){
        for(Review review : reviews){
            ReviewDTO reviewDTO = new ReviewDTO(review);
            this.results.add(reviewDTO);
        }
        totalCount = this.results.size();
    }
}
