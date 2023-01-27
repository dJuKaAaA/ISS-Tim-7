package com.tim7.iss.tim7iss.exceptions;

public class NoShiftOngoingException extends Exception {

    public NoShiftOngoingException() {
        super("No shift is ongoing!");
    }

    public NoShiftOngoingException(String message) {
        super(message);
    }
}
