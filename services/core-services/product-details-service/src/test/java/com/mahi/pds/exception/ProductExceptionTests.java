package com.mahi.pds.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductException and its subclasses
 */
@DisplayName("Product Exception Tests")
class ProductExceptionTests {

    @Test
    @DisplayName("Should create ProductException with message and error code")
    void testProductException() {
        ProductException exception = new ProductException(
            "Test error message",
            "TEST_ERROR"
        );

        assertEquals("Test error message", exception.getMessage());
        assertEquals("TEST_ERROR", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatusCode());
    }

    @Test
    @DisplayName("Should create ProductException with custom HTTP status")
    void testProductExceptionWithCustomStatus() {
        ProductException exception = new ProductException(
            "Server error",
            "SERVER_ERROR",
            500
        );

        assertEquals("Server error", exception.getMessage());
        assertEquals("SERVER_ERROR", exception.getErrorCode());
        assertEquals(500, exception.getHttpStatusCode());
    }

    @Test
    @DisplayName("Should create ProductNotFoundException")
    void testProductNotFoundException() {
        ProductNotFoundException exception = new ProductNotFoundException(123L);

        assertEquals("Product not found with id: 123", exception.getMessage());
        assertEquals("PRODUCT_NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getHttpStatusCode());
    }

    @Test
    @DisplayName("Should create ProductValidationException")
    void testProductValidationException() {
        ProductValidationException exception = new ProductValidationException(
            "price",
            "Price must be positive"
        );

        assertTrue(exception.getMessage().contains("price"));
        assertTrue(exception.getMessage().contains("Price must be positive"));
        assertEquals("VALIDATION_ERROR", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatusCode());
    }

    @Test
    @DisplayName("Should create DatabaseOperationException")
    void testDatabaseOperationException() {
        Exception cause = new RuntimeException("Connection timeout");
        DatabaseOperationException exception = new DatabaseOperationException(
            "save product",
            cause
        );

        assertTrue(exception.getMessage().contains("save product"));
        assertEquals("DATABASE_ERROR", exception.getErrorCode());
        assertEquals(500, exception.getHttpStatusCode());
        assertEquals(cause, exception.getCause());
    }
}

