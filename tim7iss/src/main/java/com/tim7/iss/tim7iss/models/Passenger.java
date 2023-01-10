package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Passenger extends User {

    @ManyToMany(mappedBy = "passengers", cascade = {CascadeType.MERGE})
    private Set<Ride> rides = new HashSet<>();
    

    public Passenger(UserDto passengerRequestBodyDto) {
        this.setFirstName(passengerRequestBodyDto.getName());
        this.setLastName(passengerRequestBodyDto.getSurname());
        this.setProfilePicture(passengerRequestBodyDto.getProfilePicture());
        this.setPhoneNumber(passengerRequestBodyDto.getTelephoneNumber());
        this.setEmailAddress(passengerRequestBodyDto.getEmail());
        this.setAddress(passengerRequestBodyDto.getAddress());
        this.setPassword(passengerRequestBodyDto.getPassword());
        this.setActive(false);
        this.setBlocked(false);
    }

}
