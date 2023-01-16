package com.tim7.iss.tim7iss.exceptions;

public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException() {
        super("Document does not exist");
    }

    public DocumentNotFoundException(String message) {
        super(message);
    }

}
