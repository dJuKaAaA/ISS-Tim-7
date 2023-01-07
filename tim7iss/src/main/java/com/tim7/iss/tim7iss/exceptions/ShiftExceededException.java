package com.tim7.iss.tim7iss.exceptions;

public class ShiftExceededException extends Exception {

    public ShiftExceededException() {
        super("Cannot start shift because you exceeded the 8 hours limit in last 24 hours!");
    }

    public ShiftExceededException(String message) {
        super(message);
    }
}
