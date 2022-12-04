package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    public List<Message> findAllBySenderId(Long userId);
    public List<Message> findAllByReceiverId(Long userId);
}
