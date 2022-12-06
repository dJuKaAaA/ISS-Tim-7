package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;

    public void save(Driver driver){
        driverRepository.save(driver);
    }

    public Collection<Driver> getAll() {
        return driverRepository.findAll();
    }

    public Page<Driver> getAll(Pageable pageable) {
        return driverRepository.findAll(pageable);
    }

    public Driver getById(Long id) {
        return driverRepository.findById(id).orElse(null);
    }

    public long countAll() {
       return driverRepository.count();
    }

}
