package com.tim7.iss.tim7iss.models;

import com.tim7.iss.tim7iss.DTOs.apidriver.WorkHourRequestBodyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
