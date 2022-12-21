package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long id;
    private String timeOfSending;  // missing in schemas in swagger
    private Long senderId;
    private Long receiverId;
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
