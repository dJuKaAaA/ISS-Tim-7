package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Notification;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Optional<List<Notification>> findAllByUser(User user);
    Optional<List<Notification>> findAllByRide(Ride ride);
}
