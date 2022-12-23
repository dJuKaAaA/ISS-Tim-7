package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmailAddress(String email);
}
