package com.tim7.iss.tim7iss.exceptions;

public class MessageNotFoundException extends Exception {

    public MessageNotFoundException() {
        super("Message not found");
    }

    public MessageNotFoundException(String message) {
        super(message);
    }
}
