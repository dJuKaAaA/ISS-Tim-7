package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Collection<Document> findByDriverId(Long driverId);

}
