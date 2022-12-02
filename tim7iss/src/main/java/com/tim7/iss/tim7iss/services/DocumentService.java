package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.models.Document;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Collection<Document> getDriverDocuments(Long driverId) {
        return documentRepository.findByDriverId(driverId);
    }

    public void save(Document document) {
        documentRepository.save(document);
    }

    public void delete(Document document) {
        documentRepository.delete(document);
    }

}
