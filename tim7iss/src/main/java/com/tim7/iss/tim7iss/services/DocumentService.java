package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.DriverDocumentDto;
import com.tim7.iss.tim7iss.exceptions.DocumentNotFoundException;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.LargeImageException;
import com.tim7.iss.tim7iss.exceptions.NotAnImageException;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Document;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.repositories.DocumentRepository;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ImageService imageService;

    public void save(Document document) {
        documentRepository.save(document);
    }

    public void deleteById(Long id) throws DocumentNotFoundException {
        Document document = documentRepository.findById(id).orElseThrow(DocumentNotFoundException::new);
        documentRepository.deleteById(id);
    }

    public Document createDocumentForDriver(DriverDocumentDto documentDto, Long driverId)
            throws DriverNotFoundException, LargeImageException, NotAnImageException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);

        if (documentDto.getDocumentImage().getBytes().length > Constants.imageFieldSize) {
            throw new LargeImageException();
        }

        // checking the validity of the passed image
        if (!imageService.isImageValid(documentDto.getDocumentImage())) {
            throw new NotAnImageException();
        }

        return documentRepository.save(new Document(documentDto, driver));

    }

}
