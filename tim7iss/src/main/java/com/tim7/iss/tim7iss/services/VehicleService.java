package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

}
