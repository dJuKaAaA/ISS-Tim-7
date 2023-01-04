package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
}
