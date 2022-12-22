package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.UserDto;
import jakarta.persistence.*;
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
@ToString
public class Driver extends User {

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    private Set<Document> documents;

    @OneToOne(mappedBy = "driver", fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    private Vehicle vehicle;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    private Set<WorkHour> workHours = new HashSet<>();

    public Driver(UserDto driverRequestBodyDto) {
        this.setFirstName(driverRequestBodyDto.getName());
        this.setLastName(driverRequestBodyDto.getSurname());
        this.setProfilePicture(driverRequestBodyDto.getProfilePicture());
        this.setPhoneNumber(driverRequestBodyDto.getTelephoneNumber());
        this.setEmailAddress(driverRequestBodyDto.getEmail());
        this.setAddress(driverRequestBodyDto.getAddress());
        this.setPassword(driverRequestBodyDto.getPassword());
        this.setActive(false);
        this.setBlocked(false);
    }

}
