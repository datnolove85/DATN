package com.example.backend.Exception;

public class VirtualTryOnException extends RuntimeException {

    public VirtualTryOnException(String message) {
        super(message);
    }

    public VirtualTryOnException(String message, Throwable cause) {
        super(message, cause);
    }
}
