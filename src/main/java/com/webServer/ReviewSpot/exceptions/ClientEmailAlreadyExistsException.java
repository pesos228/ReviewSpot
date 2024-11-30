package com.webServer.ReviewSpot.exceptions;

public class ClientEmailAlreadyExistsException extends RuntimeException{
    public ClientEmailAlreadyExistsException(String message) {
        super(message);
    }
}
