package com.tim7.iss.tim7iss.dto;

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


}