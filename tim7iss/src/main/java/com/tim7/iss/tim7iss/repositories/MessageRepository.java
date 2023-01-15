package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    public List<Message> findAllBySenderId(Long userId);
    public List<Message> findAllByReceiverId(Long userId);

    @Query(value = "SELECT  * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY sender_id,receiver_id ORDER BY sent_date DESC) rn FROM message m)x WHERE x.rn = 1 and (sender_id = ?1 or receiver_id = ?1)", nativeQuery = true)
    public List<Message> findAllByLastMessagedSent(Long userId);
}
