package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.DriverChangeDocumentRequestDto;
import com.tim7.iss.tim7iss.dto.DriverDocumentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverDocumentChangeRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private Document document; // It will be null if request for adding new document is sent
    private String documentName;
    private byte[] documentImage;

    @ManyToOne()
    @JoinColumn(name = "driver_profile_change_request_id", referencedColumnName = "id")
    private DriverProfileChangeRequest driverProfileChangeRequest;

    public DriverDocumentChangeRequest(DriverChangeDocumentRequestDto requestDto, Document document) {
        this.documentName = requestDto.getName();
        this.documentImage = requestDto.getDocumentImage().getBytes();
        this.document = document;
    }

    public DriverDocumentChangeRequest(DriverChangeDocumentRequestDto requestDto, Document document, DriverProfileChangeRequest driverProfileChangeRequest) {
        this.documentName = requestDto.getName();
        this.documentImage = requestDto.getDocumentImage().getBytes();
        this.document = document;
        this.driverProfileChangeRequest = driverProfileChangeRequest;
    }

    public DriverDocumentChangeRequest(String documentName, byte[] documentImage, Document document) {
        this.documentName = documentName;
        this.documentImage = documentImage;
        this.document = document;
    }

    public DriverDocumentChangeRequest(String documentName, byte[] documentImage, Document document,
                                       DriverProfileChangeRequest driverProfileChangeRequest) {
        this.documentName = documentName;
        this.documentImage = documentImage;
        this.document = document;
        this.driverProfileChangeRequest = driverProfileChangeRequest;
    }


}
