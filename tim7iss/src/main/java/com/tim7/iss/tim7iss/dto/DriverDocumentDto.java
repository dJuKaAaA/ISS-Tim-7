package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDocumentDto {

    @Null(message = "Id should not be provided in body")
    private Long id;
    @NotBlank(message = "Document name must be provided")
    private String name;
    @NotBlank(message = "Document image must be provided")
    private String documentImage;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Null(message = "Driver id should not be provided")
    private Long driverId;

    public DriverDocumentDto(Document document) {
        this.id = document.getId();
        this.name = document.getName();
        this.documentImage = new String(document.getPicture());
        this.driverId = document.getDriver().getId();
    }

}
