package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.dto.MessageDto;
import com.tim7.iss.tim7iss.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    public List<Message> findAllBySenderId(Long userId);
    public List<Message> findAllByReceiverId(Long userId);

    @Query(value = "SELECT  * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY sender_id,receiver_id ORDER BY sent_date DESC) rn FROM message m)x WHERE x.rn = 1 and (sender_id = ?1 or receiver_id = ?1)", nativeQuery = true)
    public List<Message> findAllByLastMessagedSent(Long userId);

    @Query(value = "select * from message where message.sent_date = (select max(sent_date) from message where (sender_id = :senderId and receiver_id = :receiverId) or (sender_id = :receiverId and receiver_id = :senderId)) and ((sender_id = :senderId and receiver_id = :receiverId) or (sender_id = :receiverId and receiver_id = :senderId))",nativeQuery = true)
    Optional<Message> findLastSentByUsers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    // vjerovatno bi trebalo da prima page, ali nemam vise zivaca iskr
    @Query(value = "select * from message where (sender_id = :senderId and receiver_id = :receiverId) or (sender_id = :receiverId and receiver_id = :senderId) order by sent_date asc;", nativeQuery = true)
    List<Message> findConversation(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    @Query(value = "SELECT m.* FROM message m JOIN (SELECT LEAST(sender_id, receiver_id) AS user_id1, GREATEST(sender_id, receiver_id) AS user_id2, MAX(sent_date) AS max_sent_date FROM message GROUP BY LEAST(sender_id, receiver_id), GREATEST(sender_id, receiver_id)) m2 ON ((m.sender_id = m2.user_id1 AND m.receiver_id = m2.user_id2 and (m.receiver_id = ?1 or sender_id = ?1)) OR (m.sender_id = m2.user_id2 AND m.receiver_id = m2.user_id1) and (m.receiver_id = ?1 or sender_id = ?1)) AND m.sent_date = m2.max_sent_date;", nativeQuery = true)
    List<Message> findLastMessage(Long id);
}
