package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Page<Passenger> findAll(Pageable page);

    Optional<Passenger> findByEmailAddress(String emailAddress);
}
