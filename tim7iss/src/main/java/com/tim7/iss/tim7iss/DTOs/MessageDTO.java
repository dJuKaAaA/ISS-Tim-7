package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Constants;
import com.tim7.iss.tim7iss.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDTO {
    private Long id;
    private String timeOfSending;
    private Long senderId;
    private Long receiverId;
    private String message;
    private String type;
    private Long rideId;

    public MessageDTO(Message message){
        this.id = message.getId();
        this.timeOfSending = message.getSentDate().format(Constants.customDateTimeFormat);
        this.senderId = message.getSender().getId();
        this.receiverId = message.getReceiver().getId();
        this.message = message.getContent();
        this.type = message.getType();
        this.rideId = message.getRide().getId();
    }
}
