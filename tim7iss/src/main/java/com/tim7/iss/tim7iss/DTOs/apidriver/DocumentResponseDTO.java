package com.tim7.iss.tim7iss.DTOs.apidriver;

import com.tim7.iss.tim7iss.models.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDTO {

    private Long id;
    private String name;
    private String documentImage;
    private Long driverId;

    public DocumentResponseDTO(Document document) {
        this.id = document.getId();
        this.name = document.getName();
        this.documentImage = document.getPicturePath();
        this.driverId = document.getDriver().getId();
    }

}
