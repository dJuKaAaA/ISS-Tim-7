package com.tim7.iss.tim7iss.exceptions;

public class WorkHourNotFoundException extends Exception {

    public WorkHourNotFoundException() {
        super("Working hour does not exist!");
    }

    public WorkHourNotFoundException(String message) {
        super(message);
    }

}
