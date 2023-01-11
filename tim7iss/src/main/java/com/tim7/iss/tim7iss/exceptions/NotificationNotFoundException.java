package com.tim7.iss.tim7iss.exceptions;

public class NotificationNotFoundException extends Exception{
    public NotificationNotFoundException() {
        super("Notification is not found!");
    }

    public NotificationNotFoundException(String message) {
        super(message);
    }
}
