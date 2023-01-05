package com.tim7.iss.tim7iss.exceptions;

public class VehicleAlreadyAssignedException extends Exception {

    public VehicleAlreadyAssignedException() {
        super("Vehicle already assigned!");
    }

    public VehicleAlreadyAssignedException(String message) {
        super(message);
    }
}
