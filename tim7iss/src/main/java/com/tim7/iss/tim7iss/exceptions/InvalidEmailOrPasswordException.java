package com.tim7.iss.tim7iss.exceptions;

public class InvalidEmailOrPasswordException extends Exception {

    public InvalidEmailOrPasswordException() {
        super("Wrong username or password!");
    }

    public InvalidEmailOrPasswordException(String message) {
        super(message);
    }

}
