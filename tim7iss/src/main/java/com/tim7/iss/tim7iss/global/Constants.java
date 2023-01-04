package com.tim7.iss.tim7iss.global;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Constants {
    public final static String stringDateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    public final static int imageFieldSize = 100000;
    public static DateTimeFormatter customDateTimeFormat = DateTimeFormatter.ofPattern(stringDateTimeFormat);

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
}
