package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRouteDto {
    public GeoCoordinateDto startLocation;
    public GeoCoordinateDto endLocation;
}
