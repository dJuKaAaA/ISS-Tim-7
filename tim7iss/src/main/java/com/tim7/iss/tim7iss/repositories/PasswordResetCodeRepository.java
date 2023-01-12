package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {

    PasswordResetCode findByUserId(Long userId);

    @Query(value = "select * from password_reset_code where creation_date like (select max(creation_date) from password_reset_code where user_id = :userId) and user_id = :userId", nativeQuery = true)
    PasswordResetCode findMostRecentByUserId(@Param("userId") Long userId);

}
