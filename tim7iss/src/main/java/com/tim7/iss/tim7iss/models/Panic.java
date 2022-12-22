package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.PanicCreateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    @NotBlank
    private String reason;

    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Panic(PanicCreateDto reason, Ride ride, User user) {
        this.reason = reason.getReason();
        this.ride = ride;
        this.user = user;
        this.sentTime = LocalDateTime.now();
    }
}
