package com.tim7.iss.tim7iss.exceptions;

import com.tim7.iss.tim7iss.models.Enums;

public class RideCancelationException extends Exception {

    public RideCancelationException() {
        super("Cannot cancel a ride that is not in status PENDING or STARTED!");
    }

    public RideCancelationException(String message) {
        super(message);
    }

}
