package com.tim7.iss.tim7iss.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "ggcj_users")
public abstract class User implements UserDetails {

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
    private boolean enabled;
    private Timestamp lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }
    @Override
    public String getUsername() {
        return this.getEmailAddress();
    }

    // TODO implementirati
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // TODO implementirati
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // TODO implementirati
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
