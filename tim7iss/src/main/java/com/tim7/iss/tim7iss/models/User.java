package com.tim7.iss.tim7iss.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "app_users")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    @SequenceGenerator(name = "id_generator", sequenceName = "user_id_seq", initialValue = 6)
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String phoneNumber;
//    @Email
    private String emailAddress;
    private String address;
    private String password;
    private boolean isBlocked;
    private boolean isActive;

    @OneToMany(mappedBy = "sender")
    private Set<Message> sentMessages = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.PERSIST)
    private Set<Message> receivedMessages = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<Refusal> refusals = new HashSet<>();

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.PERSIST)
    private Set<Review> reviews = new HashSet<>();


}
