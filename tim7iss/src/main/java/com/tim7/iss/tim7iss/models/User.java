package com.tim7.iss.tim7iss.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
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
    private boolean enabled;
    private Timestamp lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "sender")
    private Set<Message> sentMessages = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.PERSIST)
    private Set<Message> receivedMessages = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<Refusal> refusals = new HashSet<>();

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.PERSIST)
    private Set<Review> reviews = new HashSet<>();

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
