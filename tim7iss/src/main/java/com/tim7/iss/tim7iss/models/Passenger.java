package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.Member2.PassengerDTOs.PassengerRequestDTO;
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
@Builder
public class Passenger extends User {

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "finished_rides",
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

    public Passenger(PassengerRequestDTO passengerRequestDTO) {
        this.setFirstName(passengerRequestDTO.name);
        this.setLastName(passengerRequestDTO.surname);
        this.setProfilePicture(passengerRequestDTO.profilePicture);
        this.setPhoneNumber(passengerRequestDTO.telephoneNumber);
        this.setEmailAddress(passengerRequestDTO.email);
        this.setAddress(passengerRequestDTO.address);
        this.setPassword(passengerRequestDTO.password);
    }

    public void setParameters(PassengerRequestDTO passengerRequestDTO){
        this.setFirstName(passengerRequestDTO.name);
        this.setLastName(passengerRequestDTO.surname);
        this.setProfilePicture(passengerRequestDTO.profilePicture);
        this.setPhoneNumber(passengerRequestDTO.telephoneNumber);
        this.setEmailAddress(passengerRequestDTO.email);
        this.setAddress(passengerRequestDTO.address);
        this.setPassword(passengerRequestDTO.password);
    }

    @Override
    public String toString() {
        String string = "Passenger:" +
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
