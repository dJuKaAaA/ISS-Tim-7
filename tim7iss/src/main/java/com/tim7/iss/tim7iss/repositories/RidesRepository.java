package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Enums;
import com.tim7.iss.tim7iss.models.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RidesRepository extends JpaRepository<Ride, Long> {
//    public Page<Ride> FindBypassenger_idAndDateBetweenAndOrderBysortingCollumn(Long passenger_id, String sortingCollumn, String startDate, String endDate, Pageable p);
    @Query(value = "select * from ride where id in (select ride_id from finished_rides where passenger_id = ?1)", nativeQuery = true)
    List<Ride> findAll(Long passenger_id);

    @Query(value = "select r.* from ride r left join finished_rides fr on r.id = fr.ride_id where fr.passenger_id = ?1 and r.status = ?2", nativeQuery = true)
    Ride findByPassengersIdAndStatus(Long passenger_id, Integer status);
    @Query(value = "select * from ride where driver_id = ?1 and status = ?2", nativeQuery = true)
    Ride findByDriverIdAndStatus(Long driver_id, Integer status);

    @Query(value = "select * from ride where id = ?1 and status = ?2", nativeQuery = true)
    Ride findByIdAndStatus(Long id, Integer status);

    Page<Ride> findRideByPassengersId(Long id, Pageable page);
}