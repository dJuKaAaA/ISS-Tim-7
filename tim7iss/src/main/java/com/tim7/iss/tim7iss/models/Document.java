package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.DocumentRequestBodyDTO;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    private String name;
    private String picturePath;


    public Document(DocumentRequestBodyDTO documentRequestBodyDTO, Driver driver) {
        this.setName(documentRequestBodyDTO.getName());
        this.setPicturePath(documentRequestBodyDTO.getDocumentImage());
        this.driver = driver;
    }



}
