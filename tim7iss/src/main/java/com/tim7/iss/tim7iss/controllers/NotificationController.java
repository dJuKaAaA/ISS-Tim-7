package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.NotificationDto;
import com.tim7.iss.tim7iss.exceptions.NotificationNotFoundException;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.models.Notification;
import com.tim7.iss.tim7iss.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/api/notification/user/{userId}")
    public ResponseEntity<List<NotificationDto>> getUserNotifications(@PathVariable("userId") Long userId) throws UserNotFoundException, NotificationNotFoundException {
        List<Notification> notifications = notificationService.getAllUserNotifications(userId);
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto(notification);
            notificationDtoList.add(notificationDto);
        }
        return new ResponseEntity<>(notificationDtoList, HttpStatus.OK);
    }

    @GetMapping("/api/notification/ride/{rideId}")
    public ResponseEntity<List<NotificationDto>> getRideNotifications(@PathVariable("rideId") Long rideId) throws RideNotFoundException, NotificationNotFoundException {
        List<Notification> notifications = notificationService.getAllRideNotifications(rideId);
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto(notification);
            notificationDtoList.add(notificationDto);
        }
        return new ResponseEntity<>(notificationDtoList, HttpStatus.OK);
    }

    @PutMapping("/api/notification/update/{notificationId}")
    public ResponseEntity updateNotification(@PathVariable("notificationId") Long notificationId, @RequestBody NotificationDto notificationDto) throws UserNotFoundException, RideNotFoundException {
        notificationDto.setId(notificationId);
        notificationService.update(notificationDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/api/notification/save")
    public ResponseEntity saveNotification(@RequestBody NotificationDto notificationDto) throws UserNotFoundException, RideNotFoundException {
        notificationService.save(notificationDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
