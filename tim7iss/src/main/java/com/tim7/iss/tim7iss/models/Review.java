package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@Entity
@Table(name = "app_reviews")
public abstract class Review {

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
    private User passenger;
}
