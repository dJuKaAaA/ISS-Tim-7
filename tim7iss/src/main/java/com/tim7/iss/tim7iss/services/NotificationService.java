package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.NotificationDto;
import com.tim7.iss.tim7iss.exceptions.NotificationNotFoundException;
import com.tim7.iss.tim7iss.exceptions.RideNotFoundException;
import com.tim7.iss.tim7iss.exceptions.UserNotFoundException;
import com.tim7.iss.tim7iss.models.Notification;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.repositories.NotificationRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RideRepository rideRepository;


    public void save(NotificationDto notificationDto) throws UserNotFoundException {
        User user = userRepository.findById(notificationDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Ride ride = null;
        if (notificationDto.getRideId() != null) {
            ride = rideRepository.findById(notificationDto.getRideId()).orElse(null);
        }
        Notification notification = new Notification(notificationDto, user, ride);
        notificationRepository.save(notification);
    }

    public void update(NotificationDto notificationDto) throws UserNotFoundException {
        User user = userRepository.findById(notificationDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Ride ride = null;
        if (notificationDto.getRideId() != null) {
            ride = rideRepository.findById(notificationDto.getRideId()).orElse(null);
        }
        Notification notification = new Notification(notificationDto, user, ride);
        notificationRepository.save(notification);
    }

    public Notification get(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(NotFoundException::new);
    }

    public List<Notification> getAllUserNotifications(Long userId) throws UserNotFoundException, NotificationNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return notificationRepository.findAllByUser(user).orElseThrow(NotificationNotFoundException::new);
    }

    public List<Notification> getAllRideNotifications(Long rideId) throws RideNotFoundException, NotificationNotFoundException {
        Ride ride = rideRepository.findById(rideId).orElseThrow(RideNotFoundException::new);
        return notificationRepository.findAllByRide(ride).orElseThrow(NotificationNotFoundException::new);
    }
}
