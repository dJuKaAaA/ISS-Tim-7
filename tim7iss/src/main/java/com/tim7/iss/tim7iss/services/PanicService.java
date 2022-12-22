package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Panic;
import com.tim7.iss.tim7iss.repositories.PanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PanicService {
    @Autowired
    PanicRepository panicRepository;

    public void save(Panic panic){
        panicRepository.save(panic);
    }

    public List<Panic> findAll() {return panicRepository.findAll(); }

    public Long countAll() {
        return panicRepository.count();
    }
}
