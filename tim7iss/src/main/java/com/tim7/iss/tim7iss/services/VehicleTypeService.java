package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.VehicleType;
import com.tim7.iss.tim7iss.repositories.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleTypeService {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    public VehicleType getByName(String name) {
        return vehicleTypeRepository.findByName(name).orElse(null);
    }

}
