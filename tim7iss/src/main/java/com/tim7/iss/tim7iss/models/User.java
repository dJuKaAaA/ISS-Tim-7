package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
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

    @OneToMany(mappedBy = "sender")
    private Set<Message> sentMessages = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "receivers",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id")
    )
    private Set<Message> receivedMessages = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Refusal> refusals = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews = new HashSet<>();

}
