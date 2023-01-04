package com.tim7.iss.tim7iss.exceptions;

public class DriverNotFoundException extends Exception{

    public DriverNotFoundException() {
        super("Driver does not exist!");
    }

    public DriverNotFoundException(String message) {
        super(message);
    }
}
