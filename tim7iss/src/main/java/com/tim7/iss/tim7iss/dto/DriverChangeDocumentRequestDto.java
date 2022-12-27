package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.DriverDocumentChangeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverChangeDocumentRequestDto {
    private Long documentId;
    private String name;
    private String documentImage;


}