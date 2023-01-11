package com.tim7.iss.tim7iss.exceptions;

public class LocationNotFoundException extends Exception {

    public LocationNotFoundException() {
        super("Location not found");
    }

    public LocationNotFoundException(String message) {
        super(message);
    }

}
