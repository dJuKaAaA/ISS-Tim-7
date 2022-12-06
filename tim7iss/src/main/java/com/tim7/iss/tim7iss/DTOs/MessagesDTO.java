package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagesDTO {
    private int totalCount;
    private Set<MessageDTO> results = new HashSet<>();

    public MessagesDTO(Set<Message> messages){
        for(Message message:messages){
            MessageDTO messageDTO = new MessageDTO(message);
            this.results.add(messageDTO);
        }
        this.totalCount = results.size();
    }
}
