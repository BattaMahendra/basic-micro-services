package com.mahi.user.exceptions;

// Key Change: A specific exception for "not found" scenarios.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}