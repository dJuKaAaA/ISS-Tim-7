package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    @Null(message = "Id should not be provided")
    private Long id;
    @Min(value = 1, message = "Rating cannot be less than 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Float rating;

    private String comment;
    private UserRefDto passenger;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.passenger = new UserRefDto(review.getPassenger());
    }

}
