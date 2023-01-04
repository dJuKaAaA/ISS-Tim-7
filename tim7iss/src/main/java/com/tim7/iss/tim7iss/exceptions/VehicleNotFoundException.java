package com.tim7.iss.tim7iss.exceptions;

public class VehicleNotFoundException extends Exception {
    public VehicleNotFoundException() {
        super("Vehicle does not exist!");
    }

    public VehicleNotFoundException(String message) {
        super(message);
    }
}
