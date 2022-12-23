package com.tim7.iss.tim7iss.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "Price per kilometer cannot be a negative number")
    private int pricePerKm;

    @Column(unique = true)
    @NotBlank
    private String name;

}
