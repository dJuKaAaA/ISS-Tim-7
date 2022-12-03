package com.tim7.iss.tim7iss.repositories;

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

}
