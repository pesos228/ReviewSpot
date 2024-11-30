package com.webServer.ReviewSpot.exceptions;

public class MediaAlreadyExistsException extends RuntimeException{
    public MediaAlreadyExistsException(String message) {
        super(message);
    }
}
