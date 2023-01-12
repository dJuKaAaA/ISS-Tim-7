package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocationDto {

    private Long driverId;
    private GeoCoordinateDto location;
    private Boolean isActive;

    public DriverLocationDto(Driver driver) {
        this.driverId = driver.getId();
        this.location = new GeoCoordinateDto(driver.getVehicle().getLocation());
        this.isActive = driver.isActive();
    }
}
