package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.UserDto;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Passenger extends User {

    @ManyToMany
    @JoinTable(
            name = "passenger_rides",
            joinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id")
    )
    private Set<Ride> finishedRides = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favourite_routes",
            joinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "route_id", referencedColumnName = "id")
    )
    private Set<Route> favouriteRoutes = new HashSet<>();

    public void setParameters(UserDto passengerRequestDto){
        this.setFirstName(passengerRequestDto.getName());
        this.setLastName(passengerRequestDto.getSurname());
        this.setProfilePicture(passengerRequestDto.getProfilePicture());
        this.setPhoneNumber(passengerRequestDto.getTelephoneNumber());
        this.setEmailAddress(passengerRequestDto.getEmail());
        this.setAddress(passengerRequestDto.getAddress());
        this.setPassword(passengerRequestDto.getPassword());
    }

}
