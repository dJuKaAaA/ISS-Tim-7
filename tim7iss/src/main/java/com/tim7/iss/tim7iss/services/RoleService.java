package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Role;
import com.tim7.iss.tim7iss.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

}
