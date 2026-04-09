package com.mahi.user.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private String timestamp;
    private HttpStatus status;
    private String path;


    public ErrorResponse(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    //constructor
    public ErrorResponse(String message, String timestamp, HttpStatus status, String path) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.path = path;
    }

    // Getters and setters (or use Lombok if preferred)
}