package com.mahi.pds.exception;

/**
 * Base custom exception for all product-related errors
 * Provides domain-specific error handling with error codes
 */
public class ProductException extends RuntimeException {

    private final String errorCode;
    private final int httpStatusCode;

    /**
     * Constructor with message and error code
     *
     * @param message descriptive error message
     * @param errorCode unique error code for client handling
     */
    public ProductException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatusCode = 400; // Default BAD_REQUEST
    }

    /**
     * Constructor with message, error code, and HTTP status
     *
     * @param message descriptive error message
     * @param errorCode unique error code
     * @param httpStatusCode HTTP status code
     */
    public ProductException(String message, String errorCode, int httpStatusCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * Constructor with message, error code, and cause
     *
     * @param message descriptive error message
     * @param errorCode unique error code
     * @param cause the cause exception
     */
    public ProductException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatusCode = 400; // Default BAD_REQUEST
    }

    /**
     * Constructor with all parameters
     *
     * @param message descriptive error message
     * @param errorCode unique error code
     * @param httpStatusCode HTTP status code
     * @param cause the cause exception
     */
    public ProductException(String message, String errorCode, int httpStatusCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}

