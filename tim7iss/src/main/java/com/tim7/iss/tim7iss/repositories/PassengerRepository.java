package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

}
