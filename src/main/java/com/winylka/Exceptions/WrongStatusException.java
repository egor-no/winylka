package com.winylka.Exceptions;

public class WrongStatusException extends RuntimeException {

    public WrongStatusException() {
        super();
    }

    public WrongStatusException(String message) {
        super(message);
    }
}
