package com.tim7.iss.tim7iss.DTOs.apidriver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestBodyDTO {

    private String name;
    private String documentImage;
}
