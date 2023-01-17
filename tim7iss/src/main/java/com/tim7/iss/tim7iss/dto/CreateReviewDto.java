package com.tim7.iss.tim7iss.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReviewDto {

    @Min(value = 1, message = "Rating cannot be less than 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Float rating;

    @NotBlank
    @NotNull
    @NotEmpty
    private String comment;
}
