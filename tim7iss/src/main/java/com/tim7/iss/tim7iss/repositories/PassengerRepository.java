package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
