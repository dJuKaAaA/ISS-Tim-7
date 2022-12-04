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
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int estimatedTimeInMinutes;
    private boolean babyOnBoard;
    private boolean petOnBoard;
    private boolean splitFare;
    private Enums.RideStatus status;

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @OneToMany(mappedBy = "ride")
    private Set<Message> messages = new HashSet<>();

    // TODO izmeniti eager
    @ManyToMany(mappedBy = "finishedRides")
    private Set<Passenger> passengers = new HashSet<>();

    @OneToOne(mappedBy = "ride")
    private Refusal refusal;

    @OneToMany(mappedBy = "ride")
    private Set<Review> reviews = new HashSet<>();

    // TODO izmeniti eager
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", estimatedTime=" + estimatedTimeInMinutes +
                ", babyOnBoard=" + babyOnBoard +
                ", petOnBoard=" + petOnBoard +
                ", status=" + status +
                '}';
    }
}
