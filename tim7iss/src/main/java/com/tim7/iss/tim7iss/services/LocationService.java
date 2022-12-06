package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public void save(Location location) {
        locationRepository.save(location);
    }

    public Location getById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public Location getByLongitudeAndLatitude(double longitude, double latitude) {
        return locationRepository.findByLongitudeAndLatitude(longitude, latitude).orElse(null);
    }

}
