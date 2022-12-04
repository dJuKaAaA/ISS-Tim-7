package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.repositories.RidesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@Transactional
public class RidesService {
    @Autowired
    RidesRepository ridesRepository;

//    public Page<Ride> findByFilter(Long id, Integer page, Integer size, String sortingCollumn, String startDate, String endDate){
//        Pageable p = PageRequest.of(page,size);
//        return ridesRepository.FindBypassenger_idAndDateBetweenAndOrderBysortingCollumn(id,sortingCollumn,startDate,endDate, p);
//    }

    public void save(Ride ride){ ridesRepository.save(ride); }
    public List<Ride> findRideByPassengerId(Long id){
        return ridesRepository.findAll(id);
    }

    public Ride findRideByDriverIdAndStatusEquals3(Long id){
        return ridesRepository.findById(id).orElse(null);
    }
}
