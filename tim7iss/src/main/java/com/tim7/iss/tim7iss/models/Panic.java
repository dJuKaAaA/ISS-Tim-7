package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Panic {

    @Id
    private Long id;

    private LocalDateTime time;
    private String reason;

    @OneToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;

}
