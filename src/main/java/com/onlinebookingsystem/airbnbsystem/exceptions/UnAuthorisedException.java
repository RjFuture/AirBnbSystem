package com.onlinebookingsystem.airbnbsystem.exceptions;

public class UnAuthorisedException extends RuntimeException{

    public UnAuthorisedException(String message)
    {
        super(message);
    }
}
