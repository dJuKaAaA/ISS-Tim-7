package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.PaginatedResponseDto;
import com.tim7.iss.tim7iss.dto.PanicDetailsDto;
import com.tim7.iss.tim7iss.models.Panic;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.repositories.PanicRepository;
import com.tim7.iss.tim7iss.services.PanicService;
import com.tim7.iss.tim7iss.services.RideService;
import com.tim7.iss.tim7iss.services.UserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/panic")
@Transactional
@CrossOrigin
public class PanicController {
    @Autowired
    PanicService panicService;

    @Autowired
    RideService rideService;

    @Autowired
    UserService userService;

    @Autowired
    private PanicRepository panicRepository;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<PanicDetailsDto>> getPanicInstances(){
        List<Panic> panics = panicService.findAll();
        Collection<PanicDetailsDto> panicList = new ArrayList<>();
        for(Panic panic : panics){
            panicList.add(new PanicDetailsDto(panic));
        }
        return new ResponseEntity<>(new PaginatedResponseDto<>(panicList.size(), panicList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PanicDetailsDto>findById(@PathVariable Long id){
        Panic panic = panicService.findById(id);
        return new ResponseEntity<>(new PanicDetailsDto(panic), HttpStatus.OK);
    }


    @PutMapping(value = "/review/{id}")
    public ResponseEntity<String> review(@PathVariable Long id){
        Panic panic = panicService.findById(id);
        panic.setReviewed(true);
        panicService.save(panic);
        return new ResponseEntity<>("Panic succsefully reviewed", HttpStatus.OK);
    }
}
