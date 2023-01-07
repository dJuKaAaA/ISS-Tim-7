package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.WorkHour;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkHourRepository extends JpaRepository<WorkHour, Long> {

    List<WorkHour> findByDriverId(Long driverId, Pageable page);

    @Query(value = "select * from work_hour where driver_id = :driverId and start_date between :startDate and :endDate order by start_date asc", nativeQuery = true)
    List<WorkHour> findByDriverIdBetweenDates(
            @Param("driverId") Long driverId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "select * from work_hour where driver_id = :driverId and end_date is null", nativeQuery = true)
    Optional<WorkHour> findOngoingByDriverId(Long driverId);


}
