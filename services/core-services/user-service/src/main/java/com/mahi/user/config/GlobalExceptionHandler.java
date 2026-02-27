package com.mahi.user.config;

import com.mahi.user.exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> getRunTimeException(RuntimeException runtimeException){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(runtimeException.getMessage());
        errorResponse.setTimestamp( LocalDateTime.now().toString());

        return ResponseEntity.status(500).body(errorResponse);
    }
}
