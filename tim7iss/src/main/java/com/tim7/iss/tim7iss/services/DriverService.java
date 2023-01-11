package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Enums;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    public void save(Driver driver){
        driverRepository.save(driver);
    }

    public Driver findById(Long id){ return driverRepository.findById(id).orElse(null); }

    public Collection<Driver> getAll() {
        return driverRepository.findAll();
    }

    public Page<Driver> getAll(Pageable pageable) {
        return driverRepository.findAll(pageable);
    }

    public Optional<Driver> getById(Long id) {
        return driverRepository.findById(id);
    }

    public long countAll() {
       return driverRepository.count();
    }

    public Optional<Driver> getByEmailAddress(String emailAddress) {
        return driverRepository.findByEmailAddress(emailAddress);
    }

}
