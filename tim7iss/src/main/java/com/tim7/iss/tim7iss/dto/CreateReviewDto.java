package com.tim7.iss.tim7iss.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReviewDto {

    @Min(value = 1, message = "Rating cannot be less than 1")
    @Min(value = 5, message = "Rating cannot be greater than 5")
    private Float rating;

    @NotBlank
    @NotNull
    @NotEmpty
    private String comment;
}
