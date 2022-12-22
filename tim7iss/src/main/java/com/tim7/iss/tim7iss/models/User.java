package com.tim7.iss.tim7iss.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "ggcj_users")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be empty")
    private String firstName;

    @NotBlank(message = "Surname must not be empty")
    private String lastName;
    private String profilePicture;

    @Length(min = 7, max = 15)
    private String phoneNumber;

    @Column(unique = true)
    @NotBlank
    @Email
    private String emailAddress;

    private String address;

    @Pattern.List({
            @Pattern(regexp = "^(?=.*[0-9]).+", message = "Password must contain at least one number"),
            @Pattern(regexp = "^(?=.*[a-z]).+", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = "^(?=.*[A-Z]).+", message = "Password must contain at least one uppercase letter")
    })
    @Length(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Column(unique = true)
    @NotBlank
    private String password;
    private boolean isBlocked;
    private boolean isActive;

}
