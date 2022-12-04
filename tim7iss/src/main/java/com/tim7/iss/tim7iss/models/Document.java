package com.tim7.iss.tim7iss.models;

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

}
