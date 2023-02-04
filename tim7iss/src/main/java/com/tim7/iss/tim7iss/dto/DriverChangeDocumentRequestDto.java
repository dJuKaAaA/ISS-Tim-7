package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.DriverDocumentChangeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverChangeDocumentRequestDto {

    private Long documentId;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String documentImage;


    public DriverChangeDocumentRequestDto(DriverDocumentChangeRequest documentRequest) {
        this.documentId = documentRequest.getId();
        this.name = documentRequest.getDocumentName();
        this.documentImage = documentRequest.getDocumentImage();
    }
}