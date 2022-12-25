package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Passenger;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public void save(Passenger passenger) {
        passengerRepository.save(passenger);
    }

    public Page<Passenger> findAll(Pageable page) {
        return passengerRepository.findAll(page);
    }

    public List<Passenger> findAll() {
        return passengerRepository.findAll();
    }

    public Passenger findById(Long id) {
        return passengerRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {return passengerRepository.existsById(id); }

    public Long countAll() {
        return passengerRepository.count();
    }

}
