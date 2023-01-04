package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.Route;
import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    @Null(message = "Id should not be provided")
    private Long id;
    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d):([0-5]\\d)$",
            message = "Invalid date format")
    private String startTime;
    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d):([0-5]\\d)$",
            message = "Invalid date format")
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
    @Pattern(regexp="^(PENDING|ACCEPTED|REJECTED|ACTIVE|FINISHED)$",
            message = "Invalid status... Must be PENDING, ACCEPTED, REJECTED, ACTIVE or FINISHED")
    private String status;

    public RideDto(Ride ride) {
        this.id = ride.getId();
        this.startTime = ride.getStartTime().format(Constants.customDateTimeFormat);
        if (ride.getEndTime() != null) {
            this.endTime = ride.getEndTime().format(Constants.customDateTimeFormat);
        }
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
