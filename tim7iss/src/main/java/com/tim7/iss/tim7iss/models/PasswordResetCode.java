package com.tim7.iss.tim7iss.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private LocalDateTime creationDate;

    private Integer resetCodeDurationInMinutes = 30;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public PasswordResetCode(String code) {
        this.code = code;
        this.creationDate = LocalDateTime.now();
    }

    public boolean isExpired() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationDate = this.creationDate.plusMinutes(this.resetCodeDurationInMinutes);

        return currentTime.isAfter(expirationDate);
    }

}
