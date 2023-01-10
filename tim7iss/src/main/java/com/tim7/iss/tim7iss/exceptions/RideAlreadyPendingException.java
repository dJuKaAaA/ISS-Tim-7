package com.tim7.iss.tim7iss.exceptions;

public class RideAlreadyPendingException extends Exception {

    public RideAlreadyPendingException() {
        super("Cannot create a ride while you have one already pending!");
    }

    public RideAlreadyPendingException(String message) {
        super(message);
    }
}
