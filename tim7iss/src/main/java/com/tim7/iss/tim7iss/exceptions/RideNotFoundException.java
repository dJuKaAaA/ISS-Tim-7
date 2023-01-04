package com.tim7.iss.tim7iss.exceptions;

public class RideNotFoundException extends Exception {

    public RideNotFoundException() {
        super("Ride does not exist!");
    }

    public RideNotFoundException(String message) {
        super(message);
    }
}
