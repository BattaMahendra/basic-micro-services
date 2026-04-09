package com.mahi.pds.exception;

/**
 * Exception thrown when product validation fails
 */
public class ProductValidationException extends ProductException {

    public ProductValidationException(String message) {
        super(message, "VALIDATION_ERROR", 400);
    }

    public ProductValidationException(String fieldName, String reason) {
        super(String.format("Validation failed for field '%s': %s", fieldName, reason),
              "VALIDATION_ERROR", 400);
    }
}

