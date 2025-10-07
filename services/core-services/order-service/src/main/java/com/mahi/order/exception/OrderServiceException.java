package com.mahi.order.exception;



public class OrderServiceException extends RuntimeException {

    private final String errorCode;

    public OrderServiceException(String message) {
        super(message);
        this.errorCode = "ORDER_SERVICE_ERROR";
    }

    public OrderServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ORDER_SERVICE_ERROR";
    }

    public OrderServiceException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

