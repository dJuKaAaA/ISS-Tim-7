package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Panic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface PanicRepository extends JpaRepository<Panic, Integer> {
    Optional<Panic> findById(Long id);
}
