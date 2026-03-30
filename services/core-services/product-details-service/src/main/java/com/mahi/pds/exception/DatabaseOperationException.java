package com.mahi.pds.exception;

/**
 * Exception thrown when a database operation fails
 */
public class DatabaseOperationException extends ProductException {

    public DatabaseOperationException(String operation, String reason) {
        super(String.format("Database operation '%s' failed: %s", operation, reason),
              "DATABASE_ERROR", 500);
    }

    public DatabaseOperationException(String operation, Throwable cause) {
        super(String.format("Database operation '%s' failed: %s", operation, cause.getMessage()),
              "DATABASE_ERROR", 500, cause);
    }
}

