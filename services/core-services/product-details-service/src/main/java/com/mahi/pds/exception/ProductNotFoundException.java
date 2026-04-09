package com.mahi.pds.exception;

/**
 * Exception thrown when a requested product is not found
 */
public class ProductNotFoundException extends ProductException {

    public ProductNotFoundException(String message) {
        super(message, "PRODUCT_NOT_FOUND", 404);
    }

    public ProductNotFoundException(Long productId) {
        super("Product not found with id: " + productId, "PRODUCT_NOT_FOUND", 404);
    }
}

