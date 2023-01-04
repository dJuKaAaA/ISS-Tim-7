package com.tim7.iss.tim7iss.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    public PasswordResetCode(String code, LocalDateTime creationDate) {
        this.code = code;
        this.creationDate = creationDate;
    }

    public boolean isExpired() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationDate = this.creationDate.plusMinutes(this.resetCodeDurationInMinutes);

        return currentTime.isAfter(expirationDate);
    }

}
