package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.models.Constants;
import com.tim7.iss.tim7iss.models.Refusal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

// in swagger we have regular object { reason, timeOfRejection } in place of ride's attribute rejection
// instead of this object
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRejectDto {

    @NotBlank(message = "Reason cannot be blank")
    private String reason;
    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d)$",
            message = "Invalid date format")
    private String time;

    public RideRejectDto(Refusal refusal) {
        this.reason = refusal.getReason();
        this.time = refusal.getTime().format(Constants.customDateTimeFormat);
    }

}
