package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.WorkHourRequestBodyDTO;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public WorkHour(WorkHourRequestBodyDTO workHourRequestBodyDTO) {
        this.startDate = workHourRequestBodyDTO.getStart();
        this.endDate = workHourRequestBodyDTO.getEnd();
    }
}
