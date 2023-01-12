package com.tim7.iss.tim7iss.exceptions;

import com.tim7.iss.tim7iss.global.Constants;

public class LargeImageException extends Exception {

    public LargeImageException() {
        super("File is bigger than" + Constants.imageFieldSize / 1024 / 1024 + "mb!");
    }

    public LargeImageException(String message) {
        super(message);
    }
}
