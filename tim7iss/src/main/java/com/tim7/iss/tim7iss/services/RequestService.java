package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.DriverChangeDocumentRequestDto;
import com.tim7.iss.tim7iss.dto.DriverChangeProfileRequestDto;
import com.tim7.iss.tim7iss.exceptions.DocumentNotFoundException;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.models.Document;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.DriverDocumentChangeRequest;
import com.tim7.iss.tim7iss.models.DriverProfileChangeRequest;
import com.tim7.iss.tim7iss.repositories.DocumentRepository;
import com.tim7.iss.tim7iss.repositories.DriverDocumentRequestRepository;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.DriverRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RequestService {

    @Autowired
    DriverRepository driverRepository;
    @Autowired
    DriverRequestRepository driverRequestRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DriverDocumentRequestRepository driverDocumentRequestRepository;

    public ResponseEntity<DriverChangeProfileRequestDto> getRequest(Long driverId) {

        Driver driver = driverRepository.findById(driverId).get();
        DriverProfileChangeRequest request = driverRequestRepository.findByDriver(driver).get();

        // TODO dodati
        return new ResponseEntity<>(new DriverChangeProfileRequestDto(), HttpStatus.OK);
    }

    public void deleteRequest(Long requestId) {
        DriverProfileChangeRequest driverProfileChangeRequest = driverRequestRepository.findById(requestId).orElse(null);

        if (driverProfileChangeRequest != null) {
            List<DriverDocumentChangeRequest> documentRequests = driverDocumentRequestRepository.findAllByDriverProfileChangeRequest(driverProfileChangeRequest).get();

            if (!documentRequests.isEmpty()) {
                driverDocumentRequestRepository.deleteAll(documentRequests);
            }
        }
    }

    public boolean isRequestExistForDriver(Long driverId) throws DriverNotFoundException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);
        DriverProfileChangeRequest driverProfileChangeRequest = driverRequestRepository.findByDriver(driver).orElse(null);
        return driverProfileChangeRequest != null;
    }


    public HttpStatus saveRequest(Long driverId, DriverChangeProfileRequestDto requestDto) throws DriverNotFoundException, DocumentNotFoundException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);

        DriverProfileChangeRequest driverProfileChangeRequest = driverRequestRepository.findByDriver(driver).orElse(null);

        if (driverProfileChangeRequest != null) {
            List<DriverDocumentChangeRequest> documentRequests = driverDocumentRequestRepository.findAllByDriverProfileChangeRequest(driverProfileChangeRequest).get();

            if (!documentRequests.isEmpty()) {
                driverDocumentRequestRepository.deleteAll(documentRequests);
            }
        }

        Set<DriverDocumentChangeRequest> documentChangeRequests = new HashSet<>();
        for (DriverChangeDocumentRequestDto doc : requestDto.getDocuments()) {
            if (doc.getDocumentId() == null) {
                // Dodavanje novog dokumenta
                documentChangeRequests.add(new DriverDocumentChangeRequest(doc, null));
            } else {
                // Brisanje dokumenta
                Document document = documentRepository.findById(doc.getDocumentId()).orElseThrow(DocumentNotFoundException::new);
                documentChangeRequests.add(new DriverDocumentChangeRequest(null, null, document));
            }
        }
        DriverProfileChangeRequest request = driverRequestRepository.findByDriver(driver).orElse(null);
        if (request == null) {
            request = new DriverProfileChangeRequest(requestDto, driver, documentChangeRequests);
            driverRequestRepository.save(request);
        } else {
            Long id = request.getId();
            request = new DriverProfileChangeRequest(id, requestDto, driver, documentChangeRequests);
            driverRequestRepository.save(request);
        }
        for (DriverDocumentChangeRequest req : documentChangeRequests) {
            req.setDriverProfileChangeRequest(request);
            driverDocumentRequestRepository.save(req);
        }
        return HttpStatus.OK;
    }

}