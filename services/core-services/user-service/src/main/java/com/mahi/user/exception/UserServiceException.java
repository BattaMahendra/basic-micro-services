package com.mahi.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// This handles generic service-level errors (like the remote service being down)
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class UserServiceException extends RuntimeException {
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}