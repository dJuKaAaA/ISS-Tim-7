package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.DriverDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User {

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Set<Document> documents;

    @OneToOne(mappedBy = "driver", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Vehicle vehicle;

    @OneToOne
    @JoinColumn(name = "work_hours_id", referencedColumnName = "id")
    private WorkHours workHours;

    @OneToMany(mappedBy = "driver")
    private Set<Ride> rides = new HashSet<>();

}
