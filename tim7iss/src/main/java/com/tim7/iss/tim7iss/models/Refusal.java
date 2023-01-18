package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.PanicCreateDto;
import com.tim7.iss.tim7iss.dto.RideRejectDto;
import com.tim7.iss.tim7iss.global.Constants;
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
public class Refusal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nema smisla imat ovo odje
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String reason;
    private LocalDateTime time;

    @OneToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    public Refusal(RideRejectDto rideRejectDto) {
        this.reason = rideRejectDto.getReason();
        this.time = LocalDateTime.parse(rideRejectDto.getTimeOfRejection(), Constants.customDateTimeFormat);
    }

    public Refusal(PanicCreateDto panicCreateDto){
        this.reason = panicCreateDto.getReason();
        this.time = LocalDateTime.now();
    }

}
