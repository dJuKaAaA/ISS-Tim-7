package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Driver extends User {

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Document> documents;

    @OneToOne(mappedBy = "driver", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Vehicle vehicle;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<WorkHour> workHours = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(this.getId(), driver.getId());
    }

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
