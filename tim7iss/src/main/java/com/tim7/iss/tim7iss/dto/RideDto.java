package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.Route;
import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    @Null(message = "Id should not be provided")
    private Long id;
    @Future(message = "Cannot start ride at past time")
    private String startTime;
    @Future(message = "Cannot end ride at past time")
    private String endTime;
    @Min(value = 0, message = "Total cost cannot be a negative number")
    private Integer totalCost;
    private UserRefDto driver;
    private List<UserRefDto> passengers = new ArrayList<>();
    private Integer estimatedTimeInMinutes;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private RideRejectDto rejection;
    private List<LocationsForRideDto> locations = new ArrayList<>();
    @NotBlank(message = "Status must be provided")
    private String status;

    public RideDto(Ride ride) {
        this.id = ride.getId();
        this.startTime = ride.getStartTime().toString();  // TODO: Change to better date format
        this.endTime = ride.getEndTime().toString();  // TODO: Change to better date format
        this.totalCost = ride.getPrice();
        this.driver = new UserRefDto(ride.getDriver());
        for (User passenger : ride.getPassengers()) {
            this.passengers.add(new UserRefDto(passenger));
        }
        this.estimatedTimeInMinutes = ride.getEstimatedTimeInMinutes();
        this.vehicleType = ride.getVehicleType().getName();
        this.babyTransport = ride.isBabyOnBoard();
        this.petTransport = ride.isPetOnBoard();
        this.rejection = ride.getRefusal() == null ? null : new RideRejectDto(ride.getRefusal());
        for (Route route : ride.getRoutes()) {
            this.locations.add(new LocationsForRideDto(route));
        }
        this.status = ride.getStatus().toString();
    }

}
