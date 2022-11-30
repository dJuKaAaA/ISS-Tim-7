package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String profilePicture;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String password;
    private boolean isBlocked;
    private boolean isActive;

//    @OneToMany(mappedBy = "sender")
//    private Set<Message> sentMessages = new HashSet<>();
//
//    @OneToMany(mappedBy = "receiver")
//    private Set<Message> receivedMessages = new HashSet<>();
//
//    @OneToMany(mappedBy = "user")
//    private Set<Refusal> refusals = new HashSet<>();
//
//    @OneToMany(mappedBy = "user")
//    private Set<Review> reviews = new HashSet<>();

}
