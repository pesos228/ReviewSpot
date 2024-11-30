package com.webServer.ReviewSpot.exceptions;

public class ClientAlreadyHaveReactionException extends RuntimeException{
    public ClientAlreadyHaveReactionException(String message) {
        super(message);
    }
}
