package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivation {

    @Id
    private Long id;

    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;

}
