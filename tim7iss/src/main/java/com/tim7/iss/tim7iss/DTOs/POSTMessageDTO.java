package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Enums;
import com.tim7.iss.tim7iss.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class POSTMessageDTO {

    private Long receiverId;
    private String message;
    private String type;
    private Long rideId;

    public Message getMessage(){
        return Message.builder().
                sentDate(LocalDateTime.now()).
                type(Enums.MessageType.RIDE). // TODO promeniti
                content(this.message).build();
    }
}
