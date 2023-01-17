package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDto {

    private Long id;
    private String date;
    private String content;
    private boolean show;
    private Long userId;
    private Long rideId;


    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.date = notification.getDate().format(Constants.customDateTimeFormat);
        this.content = notification.getContent();
        this.show = notification.isShow();
        this.userId = notification.getUser().getId();
        this.rideId = notification.getRide().getId();
    }


}
