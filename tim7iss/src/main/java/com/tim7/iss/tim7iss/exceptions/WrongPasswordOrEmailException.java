package com.tim7.iss.tim7iss.exceptions;

public class WrongPasswordOrEmailException extends Exception{
    public WrongPasswordOrEmailException() {
        super("Wrong username or password!");
    }

    public WrongPasswordOrEmailException(String message) {
        super(message);
    }
}
