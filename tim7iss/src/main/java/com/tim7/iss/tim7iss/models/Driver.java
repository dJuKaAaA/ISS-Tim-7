package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User {

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Document> documents;

    @OneToOne(mappedBy = "driver")
    private Vehicle vehicle;

    @ManyToMany(mappedBy = "drivers", fetch = FetchType.LAZY)
    private Set<WorkHour> workHours = new HashSet<>();

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Ride> rides = new HashSet<>();

}
