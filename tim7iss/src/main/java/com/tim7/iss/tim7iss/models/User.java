package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.global.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "ggcj_users")
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(length = Constants.imageFieldSize)
    private String profilePicture;

    private String phoneNumber;

    private String emailAddress;
    private String address;
    private String password;
    private boolean isBlocked;
    private boolean isActive;
    private boolean enabled = true;


    private Timestamp lastPasswordResetDate;

    @OneToOne
    @JoinColumn(name = "password_reset_code_id", referencedColumnName = "id")
    private PasswordResetCode passwordResetCode;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
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
        return this.enabled;
    }

}
