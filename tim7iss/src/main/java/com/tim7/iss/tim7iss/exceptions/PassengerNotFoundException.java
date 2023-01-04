package com.tim7.iss.tim7iss.exceptions;

public class PassengerNotFoundException extends Exception {
    public PassengerNotFoundException() {
        super("Passenger does not exist!");
    }

    public PassengerNotFoundException(String message) {
        super(message);
    }
}
