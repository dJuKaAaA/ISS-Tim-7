package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

@MappedSuperclass
@Data
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private String profilePicture;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String password;
    private boolean isBlocked;

//    private Set<Message> sentMessages;
//    private Set<Message> receivedMessages;
//    private Set<Refusal> refusals;
//    private Set<Review> reviews;

}
