package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Constants;
import com.tim7.iss.tim7iss.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    @Null(message = "Id should not be provided in body")
    private Long id;
    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d)$",
            message = "Invalid date format")
    private String timeOfSending;
    @NotNull(message = "Sender not provided")
    private Long senderId;
    @NotNull(message = "Receiver not provided")
    private Long receiverId;
    @NotBlank(message = "No content for message provided")
    private String message;
    @Pattern(regexp="^(SUPPORT|RIDE|PANIC)$", message = "Invalid message type... Must be SUPPORT, RIDE or PANIC")
    private String type;
    @NotNull(message = "Ride id must be provided")
    private Long rideId;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.timeOfSending = message.getSentDate().format(Constants.customDateTimeFormat);
        this.senderId = message.getSender().getId();
        this.receiverId = message.getReceiver().getId();
        this.message = message.getContent();
        this.type = message.getType();
        this.rideId = message.getRide().getId();
    }

}
