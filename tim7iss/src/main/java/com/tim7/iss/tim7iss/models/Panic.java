package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.requestDTOs.PanicReasonDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

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

    private LocalDateTime time;
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
        this.time = LocalDateTime.now();
    }
}
