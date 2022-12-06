package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private int totalCount;
    private Set<UserDTO> results = new HashSet<>();

    public UsersDTO(Set<User> users){
        for(User user:users){
            UserDTO userDTO = new UserDTO(user);
            this.results.add(userDTO);
        }
        this.totalCount = this.results.size();
    }
}
