package com.tim7.iss.tim7iss.exceptions;

public class ForbiddenActionException extends Exception {
    public ForbiddenActionException() {
        super("Forbidden action");
    }

    public ForbiddenActionException(String message) {
        super(message);
    }
}
