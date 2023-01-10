package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Enums;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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

    public Collection<Ride> getByDriverId(Long driverId, Pageable page) {
        return rideRepository.findByDriverId(driverId, page);
    }

    public List<Ride> findRideByPassengerId(Long id){
        return rideRepository.findAll(id);
    }

    public Page<Ride> findRideByPassengerId(Long id, Pageable page){
        return rideRepository.findRideByPassengersId(id, page);
    }

    public Long countAllByPassengerId(Long passengerId) {
        return rideRepository.countByPassengersId(passengerId);
    }

    //Pitati zasto ovo ne radi
    public List<Ride> findByDriverIdAndStatus(Long id, Integer status){
        return rideRepository.findByDriverIdAndStatus(id, status);
    }

    public Ride findByPassengerIdAndStatus(Long id, Integer status){
        return rideRepository.findByPassengersIdAndStatus(id, status);
    }

    public Ride findByIdAndStatus(Long id, Integer status){
        return rideRepository.findByIdAndStatus(id, status);
    }

    public Ride findById(Long id){
        return rideRepository.findById(id).orElse(null);
    }

    public Ride findByStatus(Enums.RideStatus status) {
        return rideRepository.findByStatus(status);
    }
}
