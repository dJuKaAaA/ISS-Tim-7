package com.tim7.iss.tim7iss.exceptions;

public class ShiftAlreadyOngoingException extends Exception {

    public ShiftAlreadyOngoingException() {
        super("Shift already ongoing!");
    }

    public ShiftAlreadyOngoingException(String message) {
        super(message);
    }
}
