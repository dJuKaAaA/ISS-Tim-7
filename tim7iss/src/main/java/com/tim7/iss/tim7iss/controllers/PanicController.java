package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.Member2.PanicDTOs.PanicDTOList;
import com.tim7.iss.tim7iss.models.Panic;
import com.tim7.iss.tim7iss.services.PanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("api/panic")
@Transactional
public class PanicController {
    @Autowired
    PanicService panicService;

    @GetMapping
    public ResponseEntity<PanicDTOList>getPanicInstances(){
        List<Panic> panics = panicService.findAll();
        PanicDTOList panicList = new PanicDTOList();
        for(Panic panic : panics){
            panicList.addPanic(panic);
        }
        return new ResponseEntity<>(panicList, HttpStatus.OK);
    }
}
