package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Route;
import com.tim7.iss.tim7iss.repositories.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoutesService {

    @Autowired
    RoutesRepository routesRepository;

    public void saveRoutes(Set<Route>routes){
        routesRepository.saveAll(routes);
    }
}
