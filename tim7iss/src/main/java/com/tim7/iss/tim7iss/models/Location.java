package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.LocationDTO;
import com.tim7.iss.tim7iss.DTOs.Member2.LocationDTOs.LocationResponseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueLatitudeAndLongitude", columnNames = { "latitude", "longitude" })})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Min(-180)
    @Max(180)
    private double longitude;

    @Min(-90)
    @Max(90)
    private double latitude;


    public Location(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(LocationResponseDTO locationResponseDTO) {
        this.name = locationResponseDTO.getAddress();
        this.longitude = locationResponseDTO.getLongitude();
        this.latitude = locationResponseDTO.getLatitude();
    }

    public Location(com.tim7.iss.tim7iss.DTOs.apidriver.LocationDTO locationDTO) {
        this.name = locationDTO.getAddress();
        this.longitude = locationDTO.getLongitude();
        this.latitude = locationDTO.getLatitude();
    }
}
