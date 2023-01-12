package com.tim7.iss.tim7iss.exceptions;

public class VehicleNotAssignedException extends Exception {

    public VehicleNotAssignedException() {
        super("Vehicle is not assigned!");
    }

    public VehicleNotAssignedException(String message) {
        super(message);
    }
}
