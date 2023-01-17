package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoCoordinateDto {
    @NotNull
    private String address;
    @Min(value = -90, message = "Latitude cannot be less than -90")
    @Max(value = 90, message = "Latitude cannot be less than 90")
    @NotNull
    private Float latitude;
    @Min(value = -180, message = "Longitude cannot be less than -180")
    @Max(value = 180, message = "Longitude cannot be greater than 180")
    @NotNull
    private Float longitude;

    public GeoCoordinateDto(Location location) {
        this.address = location.getName();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

}
