package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
