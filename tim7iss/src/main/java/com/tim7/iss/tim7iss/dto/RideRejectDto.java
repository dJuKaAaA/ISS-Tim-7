package com.tim7.iss.tim7iss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// in swagger we have regular object { reason, timeOfRejection } in place of ride's attribute rejection
// instead of this object
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRejectDto {

    private String reason;
    private String time;

}
