package com.tim7.iss.tim7iss.exceptions;

public class SchedulingRideAtInvalidDateException extends Exception {

    public SchedulingRideAtInvalidDateException() {
        super("Invalid schedule date");
    }

    public SchedulingRideAtInvalidDateException(String message) {
        super(message);
    }
}
