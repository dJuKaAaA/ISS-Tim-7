package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.DocumentRequestBodyDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String picturePath;

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

//    @ManyToOne
//    @JoinColumn(name = "driver_id", referencedColumnName = "id")
//    private Driver driver;

    public Document(DocumentRequestBodyDTO documentRequestBodyDTO) {
        this.setName(documentRequestBodyDTO.getName());
        this.setPicturePath(documentRequestBodyDTO.getDocumentImage());
    }

}
