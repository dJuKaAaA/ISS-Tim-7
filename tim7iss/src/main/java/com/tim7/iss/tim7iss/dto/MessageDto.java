package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    @Null(message = "Id should not be provided in body")
    private Long id;
    private String timeOfSending;  // missing in schemas in swagger
    @NotNull(message = "Sender not provided")
    private Long senderId;
    @NotNull(message = "Receiver not provided")
    private Long receiverId;
    @NotBlank(message = "No content for message provided")
    private String message;
    private String type;
    private Long rideId;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.timeOfSending = message.getSentDate().toString();  // TODO: Change to better date format
        this.senderId = message.getSender().getId();
        this.receiverId = message.getReceiver().getId();
        this.message = message.getContent();
        this.type = message.getType();
        this.rideId = message.getRide().getId();
    }

}
