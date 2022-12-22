package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.DocumentRequestBodyDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotBlank(message = "Document picture is mandatory")
    private String picturePath;

    public Document(DocumentRequestBodyDTO documentRequestBodyDTO, Driver driver) {
        this.setName(documentRequestBodyDTO.getName());
        this.setPicturePath(documentRequestBodyDTO.getDocumentImage());
        this.driver = driver;
    }

}
