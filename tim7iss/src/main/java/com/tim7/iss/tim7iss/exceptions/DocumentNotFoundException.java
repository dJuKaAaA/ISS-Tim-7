package com.tim7.iss.tim7iss.exceptions;

public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException() {
        super("Document not found");
    }

    public DocumentNotFoundException(String message) {
        super(message);
    }

}
