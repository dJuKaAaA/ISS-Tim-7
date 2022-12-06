package com.tim7.iss.tim7iss.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class POSTLoginDTO {
    private String accessToken;
    private String refreshToken;
}
