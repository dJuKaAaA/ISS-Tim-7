package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
