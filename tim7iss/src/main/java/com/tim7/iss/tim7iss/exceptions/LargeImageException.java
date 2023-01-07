package com.tim7.iss.tim7iss.exceptions;

public class LargeImageException extends Exception {

    public LargeImageException() {
        super("File is bigger than 5mb!");
    }

    public LargeImageException(String message) {
        super(message);
    }
}
