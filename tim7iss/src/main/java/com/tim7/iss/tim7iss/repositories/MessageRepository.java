package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.dto.MessageDto;
import com.tim7.iss.tim7iss.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    public List<Message> findAllBySenderId(Long userId);
    public List<Message> findAllByReceiverId(Long userId);

    @Query(value = "SELECT  * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY sender_id,receiver_id ORDER BY sent_date DESC) rn FROM message m)x WHERE x.rn = 1 and (sender_id = ?1 or receiver_id = ?1)", nativeQuery = true)
    public List<Message> findAllByLastMessagedSent(Long userId);

    @Query(value = "select * from message where message.sent_date = (select max(sent_date) from message where message.sender_id = :senderId and message.receiver_id = :receiverId) and message.sender_id = :senderId and message.receiver_id = :receiverId",nativeQuery = true)
    Optional<Message> findLastSentByUsers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    // vjerovatno bi trebalo da prima page, ali nemam vise zivaca i skr
    @Query(value = "select * from message where (sender_id = :senderId and receiver_id = :receiverId) or (sender_id = :receiverId and receiver_id = :senderId) order by sent_date asc;", nativeQuery = true)
    List<Message> findConversation(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
