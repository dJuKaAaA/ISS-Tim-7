package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomTomLegsDto {
    private TomTomSummaryDto summary;
    private List<TomTomPointDto> points;
}
