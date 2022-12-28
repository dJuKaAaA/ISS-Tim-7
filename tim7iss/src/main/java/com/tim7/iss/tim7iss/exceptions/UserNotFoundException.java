package com.tim7.iss.tim7iss.exceptions;

import com.tim7.iss.tim7iss.models.User;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
