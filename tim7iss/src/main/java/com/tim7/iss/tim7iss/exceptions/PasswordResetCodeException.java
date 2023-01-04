package com.tim7.iss.tim7iss.exceptions;

public class PasswordResetCodeException extends Exception {
    public PasswordResetCodeException() {
        super("Code is expired or not correct!");
    }
    public PasswordResetCodeException(String message) {
        super(message);
    }
}
