package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewDTO {
    private Long id;
    private float rating;
    private String comment;

    private SimplePassengerDTO passenger;

    public ReviewDTO(Review review){
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.passenger = new SimplePassengerDTO((Passenger) review.getPassenger());
    }
}
