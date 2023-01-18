package com.tim7.iss.tim7iss.exceptions;

public class UnauthorizedException extends Exception{
    public UnauthorizedException () {
        super("You are unauthorized");
    }
}
