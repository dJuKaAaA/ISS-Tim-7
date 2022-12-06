package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private float rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    private User passenger; // TODO promeniti u passenger klasu
}
