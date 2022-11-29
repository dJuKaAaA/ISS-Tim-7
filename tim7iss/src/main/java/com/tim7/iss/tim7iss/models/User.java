package com.tim7.iss.tim7iss.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

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

//    @OneToMany(mappedBy = "sender")
//    private Set<Message> sentMessages;
//
//    @OneToMany(mappedBy = "receiver")
//    private Set<Message> receivedMessages;

//    @OneToMany(mappedBy = "user")
//    private Set<Refusal> refusals = new HashSet<>();
//
//    @OneToMany(mappedBy = "user")
//    private Set<Review> reviews = new HashSet<>();

}
