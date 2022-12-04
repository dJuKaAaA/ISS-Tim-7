package com.tim7.iss.tim7iss.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "driver_work_hours",
            joinColumns = @JoinColumn(name = "driver_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "work_hour_id", referencedColumnName = "id")
    )
    private Set<Driver> drivers = new HashSet<>();
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Enums.Day weekDay;

}
