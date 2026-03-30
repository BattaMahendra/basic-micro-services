package com.mahi.pds.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard error response structure for all API errors
 * Used by GlobalExceptionHandler to return consistent error responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;
    private int status;
    private String path;
    private LocalDateTime timestamp;
    private Object details;

    /**
     * Create an error response
     */
    public static ErrorResponse of(String message, String errorCode, int status) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create an error response with path
     */
    public static ErrorResponse of(String message, String errorCode, int status, String path) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Create an error response with details
     */
    public static ErrorResponse of(String message, String errorCode, int status, String path, Object details) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .path(path)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

