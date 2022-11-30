package com.tim7.iss.tim7iss.models;

import RequestDTOs.PassengerRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger extends User {

    @ManyToOne
    @JoinColumn(name = "ongoing_ride_id", referencedColumnName = "id")
    private Ride ongoingRide;

    @ManyToMany
    @JoinTable(
            name = "finished_rides",
            joinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    )
    private Set<Ride> finishedRides = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favourite_routes",
            joinColumns = @JoinColumn(name = "route_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    )
    private Set<Route> favouriteRoutes = new HashSet<>();

    public Passenger(PassengerRequestDTO passengerRequestDTO) {
        this.setFirstName(passengerRequestDTO.name);
        this.setLastName(passengerRequestDTO.surname);
        this.setProfilePicture(passengerRequestDTO.profilePicture);
        this.setPhoneNumber(passengerRequestDTO.telephoneNumber);
        this.setEmailAddress(passengerRequestDTO.email);
        this.setAddress(passengerRequestDTO.address);
        this.setPassword(passengerRequestDTO.password);
    }
}
