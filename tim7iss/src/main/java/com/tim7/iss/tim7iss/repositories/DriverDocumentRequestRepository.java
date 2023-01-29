package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.DriverDocumentChangeRequest;
import com.tim7.iss.tim7iss.models.DriverProfileChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverDocumentRequestRepository extends JpaRepository<DriverDocumentChangeRequest, Long> {

    Optional<List<DriverDocumentChangeRequest>> findAllByDriverProfileChangeRequest(DriverProfileChangeRequest driverProfileChangeRequest);
}
