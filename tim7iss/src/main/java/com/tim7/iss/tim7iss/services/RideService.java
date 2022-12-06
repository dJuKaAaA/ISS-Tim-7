package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public void save(Ride ride) {
        rideRepository.save(ride);
    }

    public long countAll() {
        return rideRepository.count();
    }

}
