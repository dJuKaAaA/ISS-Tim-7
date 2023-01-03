package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.MessageDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime sentDate;

    private String type;

    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    public Message(MessageDto messageDto, Ride ride, User sender, User receiver) {
        this.id = messageDto.getId();
        this.sentDate = LocalDateTime.parse(messageDto.getTimeOfSending(), Constants.customDateTimeFormat);
        this.type = messageDto.getType();
        this.content = messageDto.getMessage();
        this.ride = ride;
        this.sender = sender;
        this.receiver = receiver;
    }

}
