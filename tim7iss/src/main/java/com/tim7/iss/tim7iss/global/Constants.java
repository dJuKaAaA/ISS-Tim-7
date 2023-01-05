package com.tim7.iss.tim7iss.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tim7.iss.tim7iss.models.Location;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Constants {
    public final static String stringDateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    public final static int imageFieldSize = 100000;
    public static DateTimeFormatter customDateTimeFormat = DateTimeFormatter.ofPattern(stringDateTimeFormat);
    public final static int carWaitTimeInMinutes = 5;

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJsonString(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> retVal;

        try {
            retVal = mapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            retVal = null;
        }

        return retVal;
    }

    public static long calculateDistance(Location location1, Location location2) {
        float x1 = location1.getLatitude();
        float x2 = location2.getLatitude();
        float y1 = location1.getLongitude();
        float y2 = location2.getLongitude();
        return Math.round(Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(x1 - x2, 2.0)));
    }
}
