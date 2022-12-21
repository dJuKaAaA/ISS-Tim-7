package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHourDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long id;
    private String start;
    private String end;

}
