package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.DriverDocumentChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverDocumentRequestRepository extends JpaRepository<DriverDocumentChangeRequest,Long> {
}
