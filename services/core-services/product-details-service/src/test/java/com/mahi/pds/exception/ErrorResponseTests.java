package com.mahi.pds.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ErrorResponse DTO
 */
@DisplayName("Error Response Tests")
class ErrorResponseTests {

    @Test
    @DisplayName("Should create error response with of() factory method")
    void testErrorResponseFactory() {
        ErrorResponse response = ErrorResponse.of(
            "Product not found",
            "PRODUCT_NOT_FOUND",
            404
        );

        assertFalse(response.isSuccess());
        assertEquals("Product not found", response.getMessage());
        assertEquals("PRODUCT_NOT_FOUND", response.getErrorCode());
        assertEquals(404, response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Should create error response with path")
    void testErrorResponseWithPath() {
        ErrorResponse response = ErrorResponse.of(
            "Validation error",
            "VALIDATION_ERROR",
            400,
            "/api/products"
        );

        assertEquals("/api/products", response.getPath());
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Should create error response with details")
    void testErrorResponseWithDetails() {
        Object details = new Object() {
            public String field = "price";
            public String reason = "must be positive";
        };

        ErrorResponse response = ErrorResponse.of(
            "Validation failed",
            "VALIDATION_ERROR",
            400,
            "/api/products",
            details
        );

        assertNotNull(response.getDetails());
        assertEquals(details, response.getDetails());
    }

    @Test
    @DisplayName("Should have success flag set to false")
    void testErrorResponseSuccessFlag() {
        ErrorResponse response = ErrorResponse.of(
            "Error",
            "ERROR_CODE",
            500
        );

        assertFalse(response.isSuccess());
    }

    @Test
    @DisplayName("Should have timestamp")
    void testErrorResponseTimestamp() {
        ErrorResponse response = ErrorResponse.of(
            "Error",
            "ERROR_CODE",
            500
        );

        assertNotNull(response.getTimestamp());
    }
}

