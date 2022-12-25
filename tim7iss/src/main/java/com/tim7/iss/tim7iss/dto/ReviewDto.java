package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
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
