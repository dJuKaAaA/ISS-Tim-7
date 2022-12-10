package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.WorkHour;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface WorkHourRepository extends JpaRepository<WorkHour, Long> {

    List<WorkHour> findByDriverId(Long driverId, Pageable page);

}
