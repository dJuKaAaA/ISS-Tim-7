package com.tim7.iss.tim7iss.responses;

import com.tim7.iss.tim7iss.DTOs.DriverDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedDriversResponse {

    private long totalCount;
    private Collection<DriverDTO> results;

}
