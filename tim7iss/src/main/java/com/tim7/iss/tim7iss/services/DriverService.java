package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;
    public void saveDriver(Driver driver){
        driverRepository.save(driver);
    }

    public Driver findById(Long id){ return driverRepository.findById(id).orElse(null); }
}
