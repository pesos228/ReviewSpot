package com.webServer.ReviewSpot.exceptions;

public class ClientAlreadyHaveReviewException extends RuntimeException{
    public ClientAlreadyHaveReviewException(String message) {
        super(message);
    }
}
