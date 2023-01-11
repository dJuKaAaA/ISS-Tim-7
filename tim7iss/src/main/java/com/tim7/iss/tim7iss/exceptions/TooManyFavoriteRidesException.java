package com.tim7.iss.tim7iss.exceptions;

public class TooManyFavoriteRidesException extends Exception{
    public TooManyFavoriteRidesException(){ super("Number of favorite rides cannot exceed 10");}
}
