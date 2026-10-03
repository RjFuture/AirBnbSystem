package com.onlinebookingsystem.airbnbsystem.exceptions;



public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
