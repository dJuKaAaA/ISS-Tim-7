package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.PanicDetailsDto;
import com.tim7.iss.tim7iss.models.Panic;
import com.tim7.iss.tim7iss.services.PanicService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/panic")
@Transactional
public class PanicController {
    @Autowired
    PanicService panicService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<PanicDetailsDto>> getPanicInstances(){
        List<Panic> panics = panicService.findAll();
        Collection<PanicDetailsDto> panicList = new ArrayList<>();
        for(Panic panic : panics){
            panicList.add(new PanicDetailsDto(panic));
        }
        return new ResponseEntity<>(new PaginatedResponseDto<>(panicList.size(), panicList), HttpStatus.OK);
    }
}
