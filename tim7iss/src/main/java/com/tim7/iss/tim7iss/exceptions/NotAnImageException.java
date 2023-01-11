package com.tim7.iss.tim7iss.exceptions;

public class NotAnImageException extends Exception {

    public NotAnImageException() {
        super("File is not an image!");
    }

    public NotAnImageException(String message) {
        super(message);
    }
}
