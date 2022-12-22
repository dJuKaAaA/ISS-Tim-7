package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.Member2.PanicDTOs.PanicReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Panic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime sentTime;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Panic(PanicReasonDTO reason, Ride ride, User user) {
        this.reason = reason.reason;
        this.ride = ride;
        this.user = user;
        this.sentTime = LocalDateTime.now();
    }
}
