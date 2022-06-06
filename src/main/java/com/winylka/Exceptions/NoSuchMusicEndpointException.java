package com.winylka.Exceptions;

public class NoSuchMusicEndpointException extends RuntimeException {

    public NoSuchMusicEndpointException() {
        super();
    }

    public NoSuchMusicEndpointException(String message) {
        super(message);
    }
}
