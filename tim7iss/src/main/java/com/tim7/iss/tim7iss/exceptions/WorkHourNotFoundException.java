package com.tim7.iss.tim7iss.exceptions;

public class WorkHourNotFoundException extends Exception {

    public WorkHourNotFoundException() {
        super("Work hour not found");
    }

    public WorkHourNotFoundException(String message) {
        super(message);
    }

}
