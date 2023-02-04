package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.DriverDocumentChangeRequest;
import com.tim7.iss.tim7iss.models.DriverProfileChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DriverRequestRepository extends JpaRepository<DriverProfileChangeRequest, Long> {

    Optional<DriverProfileChangeRequest> findByDriver(Driver driver);


    List<DriverProfileChangeRequest> findDriverProfileChangeRequestsByStatus(String status);
}
