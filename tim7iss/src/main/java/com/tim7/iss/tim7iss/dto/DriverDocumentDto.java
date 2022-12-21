package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDocumentDto {

    private Long id;
    private String name;
    private String documentImage;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long driverId;

}
