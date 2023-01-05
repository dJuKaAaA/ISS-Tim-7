package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.WorkHour;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface WorkHourRepository extends JpaRepository<WorkHour, Long> {

    List<WorkHour> findByDriverId(Long driverId, Pageable page);

    @Query(value = "select * from work_hour where driver_id = :driverId and :date between start_date and end_date", nativeQuery = true)
    List<WorkHour> findBetweenStartDateAndEndDateByDriverId(
            @Param("driverId") Long driverId,
            @Param("date") LocalDateTime date);

}
