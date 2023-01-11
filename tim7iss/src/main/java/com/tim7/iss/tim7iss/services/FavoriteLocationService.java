package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.FavoriteLocation;
import com.tim7.iss.tim7iss.repositories.FavoriteLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FavoriteLocationService {
    @Autowired
    FavoriteLocationRepository favoriteLocationRepository;

    public FavoriteLocation save(FavoriteLocation favoriteLocation){
        return favoriteLocationRepository.save(favoriteLocation);
    }

    public List<FavoriteLocation> getAll(){
        return favoriteLocationRepository.findAll();
    }

    public void delete(Long id){
        favoriteLocationRepository.deleteById(id);
    }

    public FavoriteLocation findById(Long id) {
        return favoriteLocationRepository.findById(id).orElse(null);
    }

    public List<FavoriteLocation> findByPassengerId(Long id){
        return favoriteLocationRepository.findByPassengersId(id);
    }
}
