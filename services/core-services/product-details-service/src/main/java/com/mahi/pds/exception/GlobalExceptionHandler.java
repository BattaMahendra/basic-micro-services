package com.mahi.pds.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global Exception Handler for all REST endpoints
 *
 * Provides centralized exception handling across the application:
 * - Custom product exceptions (ProductException, ProductNotFoundException, etc.)
 * - Validation exceptions (JSR-303, ConstraintViolation)
 * - HTTP exceptions (404, 500, etc.)
 * - Generic runtime exceptions
 *
 * All exceptions are converted to standardized ErrorResponse format
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle ProductNotFoundException - Product resource not found
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {

        log.warn("Product not found: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            ex.getErrorCode(),
            ex.getHttpStatusCode(),
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle ProductValidationException - Product validation failed
     */
    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<ErrorResponse> handleProductValidationException(
            ProductValidationException ex, WebRequest request) {

        log.warn("Product validation failed: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            ex.getErrorCode(),
            ex.getHttpStatusCode(),
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle DatabaseOperationException - Database operation failed
     */
    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseOperationException(
            DatabaseOperationException ex, WebRequest request) {

        log.error("Database operation failed: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            ex.getErrorCode(),
            ex.getHttpStatusCode(),
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle generic ProductException - General product operation error
     */
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(
            ProductException ex, WebRequest request) {

        log.warn("Product exception occurred: {} [Code: {}]", ex.getMessage(), ex.getErrorCode());

        HttpStatus httpStatus = HttpStatus.valueOf(ex.getHttpStatusCode());

        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            ex.getErrorCode(),
            ex.getHttpStatusCode(),
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Handle JSR-303 validation errors from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.warn("Validation error: {}", ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(
                error.getField(),
                error.getDefaultMessage() != null ? error.getDefaultMessage() : "Validation failed"
            )
        );

        ErrorResponse errorResponse = ErrorResponse.of(
            "Validation failed for one or more fields",
            "VALIDATION_ERROR",
            400,
            request.getDescription(false).replace("uri=", ""),
            fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle constraint violation exceptions
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        log.warn("Constraint violation: {}", ex.getMessage());

        Map<String, String> violations = ex.getConstraintViolations()
            .stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage
            ));

        ErrorResponse errorResponse = ErrorResponse.of(
            "Constraint violation detected",
            "CONSTRAINT_VIOLATION",
            400,
            request.getDescription(false).replace("uri=", ""),
            violations
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle IllegalArgumentException - Invalid argument provided
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        log.warn("Illegal argument: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage() != null ? ex.getMessage() : "Invalid argument provided",
            "INVALID_ARGUMENT",
            400,
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle NullPointerException - Null reference encountered
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(
            NullPointerException ex, WebRequest request) {

        log.error("Null pointer exception: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
            "Null value encountered in processing",
            "NULL_POINTER",
            500,
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle 404 Not Found exceptions
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {

        log.warn("Resource not found: {} {}", ex.getHttpMethod(), ex.getRequestURL());

        ErrorResponse errorResponse = ErrorResponse.of(
            String.format("Endpoint %s %s not found", ex.getHttpMethod(), ex.getRequestURL()),
            "ENDPOINT_NOT_FOUND",
            404,
            ex.getRequestURL()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle generic RuntimeException - Unexpected runtime error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred",
            "RUNTIME_ERROR",
            500,
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle all other exceptions - Catch-all for unexpected errors
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {

        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
            "An unexpected error occurred. Please try again later.",
            "GENERAL_ERROR",
            500,
            request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

