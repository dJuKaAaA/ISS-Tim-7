package com.tim7.iss.tim7iss.exceptions;

public class ActivationNotFoundException extends Exception{

    public ActivationNotFoundException() {
        super("Activation with entered id does not exist!");
    }

    public ActivationNotFoundException(String message) {
        super(message);
    }
}
