package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.TomTomResponseDto;
import com.tim7.iss.tim7iss.dto.TomTomSummaryDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MapService {
    private final String API_URL_FIRST_PART = "https://api.tomtom.com//routing/1/calculateRoute/";
    private final String API_URL_SECOND_PART = "/json?key=urES86sMdjoeMbhSLu9EK3ksu0Jjpb91&travelMode=car";
    private final RestTemplate restTemplate;

    public MapService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Integer getDistance(float startLatitude, float startLongitude, float endLatitude, float endLongitude){
        ResponseEntity<TomTomResponseDto>route = getRoute(startLatitude, startLongitude, endLatitude, endLongitude);
        TomTomSummaryDto summary = getSummary(route.getBody());
        System.out.println(summary);
        return summary.getLengthInMeters();
    }

    public TomTomSummaryDto getSummary(TomTomResponseDto response){
        return response.getRoutes().get(0).getSummary();
    }

    public ResponseEntity<TomTomResponseDto>getRoute(float startLatitude, float startLongitude, float endLatitude, float endLongitude){
        String route = String.valueOf(startLatitude) + "," + String.valueOf(startLongitude) + ":" + String.valueOf(endLatitude) + "," + String.valueOf(endLongitude);
        ResponseEntity<TomTomResponseDto> response = restTemplate.exchange(API_URL_FIRST_PART + route + API_URL_SECOND_PART, HttpMethod.GET, null, new ParameterizedTypeReference<TomTomResponseDto>() {});
        return response;
    }
}
