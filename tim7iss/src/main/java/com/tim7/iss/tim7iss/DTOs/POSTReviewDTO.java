package com.tim7.iss.tim7iss.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class POSTReviewDTO {
    private int rating;
    private String comment;
}
