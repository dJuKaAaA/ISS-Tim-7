package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.DTOs.AssumedRideCostDTO;
import com.tim7.iss.tim7iss.DTOs.OrderRideDTO;
import org.springframework.stereotype.Service;

@Service
public class UnregisteredUserService {

    public AssumedRideCostDTO getAssumedRideCost(OrderRideDTO orderRideDTO) {
        return new AssumedRideCostDTO(400, 400);
    }
}
