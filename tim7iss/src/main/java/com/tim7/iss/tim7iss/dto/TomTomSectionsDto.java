package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomTomSectionsDto {
    private Integer startPointIndex;
    private Integer endPointIndex;
    private String sectionType;
    private String travelMode;
}
