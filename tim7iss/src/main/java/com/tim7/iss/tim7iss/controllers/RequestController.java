package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.DriverChangeDocumentRequestDto;
import com.tim7.iss.tim7iss.dto.DriverChangeProfileDriverRequestDto;
import com.tim7.iss.tim7iss.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RequestController {

    @Autowired
    RequestService requestService;

    @GetMapping("/api/request/{driverId}")
//    public ResponseEntity<> getRequest(@PathVariable Long driverId) {
//        return null;
//    }

    @PostMapping("/api/request/{driverId}")
    public void saveRequest(@PathVariable Long driverId, @RequestBody DriverChangeProfileDriverRequestDto requestDto) {
        requestService.saveRequest(driverId,requestDto);

    }

}
