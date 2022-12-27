package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Refusal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

// in swagger we have regular object { reason, timeOfRejection } in place of ride's attribute rejection
// instead of this object
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRejectDto {

    @NotBlank(message = "Reason cannot be blank")
    private String reason;
    private String time;

    public RideRejectDto(Refusal refusal) {
        this.reason = refusal.getReason();
        this.time = refusal.getTime().toString();  // TODO: Change to better date format
    }

}
