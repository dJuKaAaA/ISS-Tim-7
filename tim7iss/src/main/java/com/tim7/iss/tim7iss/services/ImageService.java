package com.tim7.iss.tim7iss.services;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class ImageService {

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isImageValid(String imageString) {
        boolean valid = true;
        try {
            // Decode the Base64-encoded string
            byte[] bytes = DatatypeConverter.parseBase64Binary(imageString);

            // Check if the byte array represents a valid image
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            if (image == null) {
                valid = false;
            }
        } catch (IllegalArgumentException | IOException e) {
            valid = false;
        }
        return valid;
    }

}
