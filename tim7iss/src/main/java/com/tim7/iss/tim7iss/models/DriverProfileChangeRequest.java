package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.DriverChangeProfileRequestDto;
import com.tim7.iss.tim7iss.global.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverProfileChangeRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(length = Constants.imageFieldSize)
    private String profilePicture;
    private String phoneNumber;
    private String email;
    private String address;
    private boolean isMessageDisplayed; // Is driver seen this message if his status is accepted or rejected
    private String status; // PENDING, ACCEPTED, REJECTED

    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @OneToMany(mappedBy = "driverProfileChangeRequest")
    private Set<DriverDocumentChangeRequest> driverDocumentChangeRequests;

    public DriverProfileChangeRequest(DriverChangeProfileRequestDto requestDto, Driver driver,
                                      Set<DriverDocumentChangeRequest> driverDocumentChangeRequests) {
        this.firstName = requestDto.getFirstName();
        this.lastName = requestDto.getLastName();
        this.profilePicture = requestDto.getProfilePicture();
        this.email = requestDto.getEmail();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.address = requestDto.getAddress();
        this.isMessageDisplayed = requestDto.isMessageDisplayed();
        this.status = requestDto.getStatus();

        this.driver = driver;
        this.driverDocumentChangeRequests = driverDocumentChangeRequests;

    }

    public DriverProfileChangeRequest(Long id, DriverChangeProfileRequestDto requestDto, Driver driver,
                                      Set<DriverDocumentChangeRequest> driverDocumentChangeRequests) {
        this.id = id;
        this.firstName = requestDto.getFirstName();
        this.lastName = requestDto.getLastName();
        this.profilePicture = requestDto.getProfilePicture();
        this.email = requestDto.getEmail();
        this.address = requestDto.getAddress();
        this.isMessageDisplayed = requestDto.isMessageDisplayed();
        this.status = requestDto.getStatus();
        this.driver = driver;
        this.driverDocumentChangeRequests = driverDocumentChangeRequests;

    }
    public DriverProfileChangeRequest(User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.profilePicture = user.getProfilePicture();
        this.email = user.getEmailAddress();
        this.address = user.getAddress();
        this.isMessageDisplayed = false;
        this.status = "PENDING";
        this.driver = driver;
        this.driverDocumentChangeRequests = new HashSet<>();
    }

}
