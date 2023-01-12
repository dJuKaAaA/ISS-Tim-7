package com.tim7.iss.tim7iss.exceptions;

public class EmailAlreadyExistsException extends Exception {

    public EmailAlreadyExistsException() {
        super("User with that email already exists!");
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

}
