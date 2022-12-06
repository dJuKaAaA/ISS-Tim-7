package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.DriverRequestBodyDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User {

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Document> documents;

    @OneToOne(mappedBy = "driver", fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<WorkHour> workHours = new HashSet<>();

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Ride> rides = new HashSet<>();

    public Driver(DriverRequestBodyDTO driverRequestBodyDTO) {
        this.setFirstName(driverRequestBodyDTO.getName());
        this.setLastName(driverRequestBodyDTO.getSurname());
        this.setProfilePicture(driverRequestBodyDTO.getProfilePicture());
        this.setPhoneNumber(driverRequestBodyDTO.getTelephoneNumber());
        this.setEmailAddress(driverRequestBodyDTO.getEmail());
        this.setAddress(driverRequestBodyDTO.getAddress());
        this.setPassword(driverRequestBodyDTO.getPassword());
    }
}
