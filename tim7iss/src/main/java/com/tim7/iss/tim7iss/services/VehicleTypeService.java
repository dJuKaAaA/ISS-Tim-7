package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.VehicleType;
import com.tim7.iss.tim7iss.repositories.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleTypeService {
    @Autowired
    VehicleTypeRepository vehicleTypeRepository;
    public void save(VehicleType vehicleType){
        vehicleTypeRepository.save(vehicleType);
    }

    public VehicleType findById(Long id){
        return vehicleTypeRepository.findById(id).orElse(null);
    }

    public VehicleType getByName(String name) {
        return vehicleTypeRepository.findByName(name).orElse(null);
    }

    public List<VehicleType> findAll() { return vehicleTypeRepository.findAll(); }

}
