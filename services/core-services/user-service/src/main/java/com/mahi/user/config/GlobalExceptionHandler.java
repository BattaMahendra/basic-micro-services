package com.mahi.user.config;

import com.mahi.user.exceptions.ErrorResponse;
import com.mahi.user.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> getNoResourceFoundException(ResourceNotFoundException ex, WebRequest request){

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString());
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

    }



}
