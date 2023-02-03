package com.tim7.iss.tim7iss.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tim7.iss.tim7iss.exceptions.NotAnImageException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Constants {
    public final static String stringDateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    public final static int imageFieldSize = 1024 * 1024 * 5;
    public static DateTimeFormatter customDateTimeFormat = DateTimeFormatter.ofPattern(stringDateTimeFormat);
    public final static int vehicleWaitTimeInMinutes = 5;
    public final static int workDurationInHours = 8;

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

    public static byte[] getPlaceHolderProfilePicture() throws IOException {
        String pathToPlaceholder = "src/main/resources/static/passenger10.jpg";
        return pictureStringToBytes(pathToPlaceholder);
    }

    public static byte[] pictureStringToBytes(String picturePath) throws IOException {
        return Files.readAllBytes(Paths.get(picturePath));
    }


}
