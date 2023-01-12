package com.tim7.iss.tim7iss.exceptions;

public class ActivationExpiredException extends Exception{

    public ActivationExpiredException() {
        super("Activation expired. Register again!");
    }

    public ActivationExpiredException(String message) {
        super(message);
    }
}
