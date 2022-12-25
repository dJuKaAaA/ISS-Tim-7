package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.PanicCreateDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "Panic reason cannot be empty")
    private String reason;

    @NotNull(message = "Ride is mandatory")
    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    @NotNull(message = "Panic must have a sender")
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
