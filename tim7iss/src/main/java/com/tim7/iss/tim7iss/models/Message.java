package com.tim7.iss.tim7iss.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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


    @Override
    public String toString() {
        String string = "";
        string = "Message:" +
                "id=" + id +
                ", sentDate=" + sentDate +
                ", type=" + type +
                ", content='" + content;
        if (ride != null) {
            string = string + ", sender=" + sender.getId();
        }
        if (receiver != null) {
            string = string + ", receiver=" + receiver.getId();
        }

        if (ride != null) {
            string = string + ", ride=" + ride.getId();
        }
        return string;

    }

}
