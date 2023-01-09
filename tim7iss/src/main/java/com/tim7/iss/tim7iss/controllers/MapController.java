package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.SimpleRouteDto;
import com.tim7.iss.tim7iss.dto.SimpleRouteInfoDto;
import com.tim7.iss.tim7iss.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MapController {

    @Autowired
    MapService mapService;

    // TODO proveriti
    @PostMapping(value = "/api/map/routeInfo")
    public ResponseEntity<SimpleRouteInfoDto> getDistanceAndTravelTime(@RequestBody List<SimpleRouteDto> routes) {
        Integer distance = 0;
        Integer travelTimeInSeconds = 0;
        for (SimpleRouteDto routeDto : routes) {
            distance = mapService.getDistance(routeDto.getStartLocation().getLatitude(), routeDto.getStartLocation().getLongitude(), routeDto.getEndLocation().getLatitude(), routeDto.getEndLocation().getLongitude());
            travelTimeInSeconds = mapService.getTravelTimeInSeconds(routeDto.getStartLocation().getLatitude(), routeDto.getStartLocation().getLongitude(), routeDto.getEndLocation().getLatitude(), routeDto.getEndLocation().getLongitude());

        }
        SimpleRouteInfoDto simpleRouteInfoDto = new SimpleRouteInfoDto(distance, travelTimeInSeconds);
        return new ResponseEntity<>(simpleRouteInfoDto, HttpStatus.OK);
    }
}
