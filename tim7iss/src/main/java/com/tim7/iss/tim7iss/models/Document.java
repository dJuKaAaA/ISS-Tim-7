package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.dto.DriverDocumentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Document must be assigned to a driver")
    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @NotBlank(message = "Document name is mandatory")
    private String name;

    @Column(length = Constants.imageFieldSize)
    private byte[] picture;

    public Document(DriverDocumentDto driverDocumentDto, Driver driver) {
        this.setName(driverDocumentDto.getName());
        this.setPicture(driverDocumentDto.getDocumentImage().getBytes());
        this.driver = driver;
    }

}
