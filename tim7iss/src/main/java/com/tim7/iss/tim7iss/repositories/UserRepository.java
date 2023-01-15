package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmailAddress(String email);

    @Query(value = "select distinct u.* from ggcj_users u left join message m on u.id = m.sender_id or u.id = m.receiver_id where (m.receiver_id = ?1 or m.sender_id = ?1) and u.id != ?1", nativeQuery = true)
    List<User> findByReceivedMessages(Long driver_id);

}
