package com.tim7.iss.tim7iss.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
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

}
