package com.tim7.iss.tim7iss.exceptions;

public class FavoriteLocationNotFoundException extends Exception {

    public FavoriteLocationNotFoundException() {
        super("Favorite location not found!");
    }

    public FavoriteLocationNotFoundException(String message) {
        super(message);
    }

}
