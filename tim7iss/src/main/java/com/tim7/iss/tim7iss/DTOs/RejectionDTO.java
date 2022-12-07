package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Refusal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RejectionDTO {
    private String reason = "";
    private String timeOfRejection = "";

    public RejectionDTO(Refusal refusal) {
        if (refusal != null) {
            this.reason = refusal.getReason();
            this.reason = refusal.getTime().toString();
        }
    }
}
