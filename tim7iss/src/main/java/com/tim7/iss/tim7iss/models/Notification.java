package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.NotificationDto;
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
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private String content;
    private boolean isDisplayed;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    public Notification(NotificationDto notificationDto, User user, Ride ride) {
        this.id = notificationDto.getId();
        this.date = LocalDateTime.parse(notificationDto.getDate(), Constants.customDateTimeFormat);
        this.content = notificationDto.getContent();
        this.isDisplayed = notificationDto.isShow();
        this.user = user;
        this.ride = ride;
    }


}
