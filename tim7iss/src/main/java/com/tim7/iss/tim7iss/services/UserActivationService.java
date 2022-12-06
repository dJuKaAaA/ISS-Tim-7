package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.models.UserActivation;
import com.tim7.iss.tim7iss.repositories.UserActivationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActivationService {
    @Autowired
    UserActivationRepository userActivationRepository;
    public UserActivation findById(Long id) {return userActivationRepository.findById(id).orElse(null);}

    public void deleteById(Long id) {userActivationRepository.deleteById(id);}

    public void save(UserActivation userActivation) {userActivationRepository.save(userActivation);}
}
