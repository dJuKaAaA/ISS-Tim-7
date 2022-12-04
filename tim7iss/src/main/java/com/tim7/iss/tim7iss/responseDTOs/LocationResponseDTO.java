package com.tim7.iss.tim7iss.responseDTOs;

import com.tim7.iss.tim7iss.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDTO {
    String address;
    Double latitude;
    Double longitude;
    public LocationResponseDTO(Location location){
        this.address = location.getName();
        this.longitude = location.getX();
        this.latitude = location.getY();
    }
}
