package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private float rating;
    private String review;

    public ReviewDTO(Review review){
        this.id = review.getId();
        this.rating = review.getRating();
        this.review = review.getComment();
    }
}
