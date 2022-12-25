package com.tim7.iss.tim7iss.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Refusal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User who refused is mandatory")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotBlank(message = "Refusal reason cannot be empty")
    private String reason;
    private LocalDateTime time;

    @NotNull(message = "Ride is mandatory")
    @OneToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;

}
