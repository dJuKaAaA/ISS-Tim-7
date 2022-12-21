package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long senderId;
    private Long receiverId;
    private String message;
    private String type;
    private Long rideId;

}
