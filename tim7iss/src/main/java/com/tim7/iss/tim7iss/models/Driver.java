package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToMany(mappedBy = "drivers", fetch = FetchType.LAZY)
    private Set<WorkHour> workHours = new HashSet<>();

    // cascade perzist, cascade refresh
    @OneToMany(mappedBy = "driver")
    private Set<Ride> rides = new HashSet<>();


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
