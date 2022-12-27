package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.DriverChangeProfileDriverRequestDto;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private byte[] profilePicture;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private boolean isMessageDisplayed; // Is driver seen this message if his status is accepted or rejected
    private String status; // PADDING ACCEPTED, REJECTED

    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @OneToMany(mappedBy = "driverProfileChangeRequest")
    private Set<DriverDocumentChangeRequest> driverDocumentChangeRequests;

    public DriverProfileChangeRequest(DriverChangeProfileDriverRequestDto requestDto, Driver driver,
                                      Set<DriverDocumentChangeRequest> driverDocumentChangeRequests) {
        this.firstName = requestDto.getFirstName();
        this.lastName = requestDto.getLastName();
        this.profilePicture = requestDto.getProfilePicture().getBytes();
        this.email = requestDto.getEmail();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.address = requestDto.getAddress();
        this.password = requestDto.getPassword();
        this.isMessageDisplayed = false;
        this.status = "PADDING";
        this.driver = driver;
        this.driverDocumentChangeRequests = driverDocumentChangeRequests;

    }

    public DriverProfileChangeRequest(Long id, DriverChangeProfileDriverRequestDto requestDto, Driver driver,
                                      Set<DriverDocumentChangeRequest> driverDocumentChangeRequests) {
        this.id = id;
        this.firstName = requestDto.getFirstName();
        this.lastName = requestDto.getLastName();
        this.profilePicture = requestDto.getProfilePicture().getBytes();
        this.email = requestDto.getEmail();
        this.address = requestDto.getAddress();
        this.password = requestDto.getPassword();
        this.isMessageDisplayed = false;
        this.status = "PADDING";
        this.driver = driver;
        this.driverDocumentChangeRequests = driverDocumentChangeRequests;

    }

}
