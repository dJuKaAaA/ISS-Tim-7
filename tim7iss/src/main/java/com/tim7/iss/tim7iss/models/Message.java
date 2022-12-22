package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.MessageDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
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

    @NotBlank
    private String type;

    @NotBlank
    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    public Message(MessageDto messageDto, Ride ride, User sender, User receiver) {
        this.id = messageDto.getId();
        this.sentDate = LocalDateTime.now(); // TODO promeniti
        this.type = messageDto.getType();
        this.content = messageDto.getMessage();
        this.ride = ride;
        this.sender = sender;
        this.receiver = receiver;
    }

}
