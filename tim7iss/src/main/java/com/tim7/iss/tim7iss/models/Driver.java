package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import com.tim7.iss.tim7iss.DTOs.apidriver.DriverRequestBodyDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

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
        this.setActive(false);
        this.setBlocked(false);
    }

    @Override
    public String toString() {
        String string = "Driver:" +
                "id=" + this.getId() +
                ", firstName='" + this.getFirstName() + '\'' +
                ", lastName='" + this.getLastName() + '\'' +
                ", profilePicture='" + this.getProfilePicture() + '\'' +
                ", phoneNumber='" + this.getPhoneNumber() + '\'' +
                ", emailAddress='" + this.getAddress() + '\'' +
                ", address='" + this.getAddress() + '\'' +
                ", password='" + this.getPassword() + '\'' +
                ", isBlocked=" + this.isBlocked() +
                ", isActive=" + this.isActive();

        return string;
    }
}
