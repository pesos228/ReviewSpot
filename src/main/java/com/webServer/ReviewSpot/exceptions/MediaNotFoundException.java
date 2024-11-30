package com.webServer.ReviewSpot.exceptions;

public class MediaNotFoundException extends RuntimeException{
    public MediaNotFoundException(String message) {
        super(message);
    }
}
