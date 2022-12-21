package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ReviewDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long id;
    private Float rating;
    private String comment;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private UserRefDto ride;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private UserRefDto passenger;

}
