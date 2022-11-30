package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;

    public void save(Passenger passnger) {
        passengerRepository.save(passnger);
    }

    public List<Passenger> findAll() {
        return passengerRepository.findAll();
    }
}