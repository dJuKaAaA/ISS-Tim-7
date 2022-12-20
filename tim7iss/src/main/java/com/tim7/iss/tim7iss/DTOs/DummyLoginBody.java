package com.tim7.iss.tim7iss.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DummyLoginBody {

    private String token;
    private String email;
    private String password;

}
