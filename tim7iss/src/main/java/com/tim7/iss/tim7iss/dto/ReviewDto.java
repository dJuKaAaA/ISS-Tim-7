package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    @Null(message = "Id should not be provided")
    private Long id;
    @Min(value = 1, message = "Rating cannot be less than 1")
    @Min(value = 5, message = "Rating cannot be greater than 5")
    private Float rating;
    private String comment;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private UserRefDto ride;  // why is the instance of UserRefDto named ride in swagger
    private UserRefDto passenger;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.passenger = new UserRefDto(review.getPassenger());
    }

}
