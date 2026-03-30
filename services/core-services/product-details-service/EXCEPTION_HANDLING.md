# Exception Handling Architecture

## Overview
This document explains the enterprise-grade exception handling architecture implemented in the Product Details Service.

## Package Structure
```
com.mahi.pds.exception/
├── ProductException.java (Base exception)
├── ProductNotFoundException.java (Resource not found - HTTP 404)
├── ProductValidationException.java (Validation failure - HTTP 400)
├── DatabaseOperationException.java (DB operation failure - HTTP 500)
├── ErrorResponse.java (Standard error response DTO)
└── GlobalExceptionHandler.java (Centralized exception handler)
```

## Exception Hierarchy

```
RuntimeException
└── ProductException (Base custom exception)
    ├── ProductNotFoundException (404)
    ├── ProductValidationException (400)
    └── DatabaseOperationException (500)
```

## Exception Classes

### 1. ProductException
**Purpose:** Base exception for all product-related errors

**Features:**
- Unique error codes for client-side error handling
- HTTP status code mapping
- Cause exception tracking for debugging
- Constructors for flexibility

**Usage:**
```java
throw new ProductException(
    "Operation failed",
    "OPERATION_FAILED",
    500,
    cause
);
```

### 2. ProductNotFoundException
**Purpose:** Thrown when a product resource is not found

**Features:**
- Automatically sets HTTP 404 status
- Can be constructed with Long productId
- Error code: PRODUCT_NOT_FOUND

**Usage:**
```java
throw new ProductNotFoundException(productId);
// or
throw new ProductNotFoundException("Product not found");
```

### 3. ProductValidationException
**Purpose:** Thrown when product validation fails

**Features:**
- Automatically sets HTTP 400 status
- Constructor supports field name and reason
- Error code: VALIDATION_ERROR
- User-friendly error messages

**Usage:**
```java
throw new ProductValidationException("price", "Price must be positive");
```

### 4. DatabaseOperationException
**Purpose:** Thrown when database operations fail

**Features:**
- Automatically sets HTTP 500 status
- Captures operation type and cause
- Error code: DATABASE_ERROR
- Provides detailed error context

**Usage:**
```java
throw new DatabaseOperationException("save product", cause);
```

### 5. ErrorResponse
**Purpose:** Standardized error response DTO

**Fields:**
```json
{
  "success": false,
  "message": "Error message",
  "errorCode": "ERROR_CODE",
  "status": 400,
  "path": "/api/products/1",
  "timestamp": "2026-03-02T10:30:00",
  "details": {}
}
```

**Factory Methods:**
- `ErrorResponse.of(message, errorCode, status)`
- `ErrorResponse.of(message, errorCode, status, path)`
- `ErrorResponse.of(message, errorCode, status, path, details)`

## GlobalExceptionHandler

### Purpose
Centralized exception handling for all REST endpoints

### Features
- Handles custom product exceptions
- Handles JSR-303 validation errors (@Valid)
- Handles constraint violations
- Handles HTTP 404 errors
- Handles generic runtime exceptions
- Comprehensive logging
- Standardized error responses
- Request path tracking

### Exception Handlers

#### 1. ProductNotFoundException Handler
- Status: 404 NOT_FOUND
- Logs warning message
- Returns structured error response

#### 2. ProductValidationException Handler
- Status: 400 BAD_REQUEST
- Logs validation failure
- Returns structured error response

#### 3. DatabaseOperationException Handler
- Status: 500 INTERNAL_SERVER_ERROR
- Logs error with stack trace
- Returns structured error response

#### 4. ProductException Handler (Generic)
- Status: Dynamic based on exception's httpStatusCode
- Logs warning message
- Returns structured error response

#### 5. MethodArgumentNotValidException Handler
- Status: 400 BAD_REQUEST
- Extracts field-level validation errors
- Returns field error details
- Example response:
```json
{
  "success": false,
  "message": "Validation failed for one or more fields",
  "errorCode": "VALIDATION_ERROR",
  "status": 400,
  "path": "/api/products",
  "details": {
    "title": "must not be null",
    "price": "must be greater than 0"
  },
  "timestamp": "2026-03-02T10:30:00"
}
```

#### 6. ConstraintViolationException Handler
- Status: 400 BAD_REQUEST
- Extracts path-level constraint violations
- Returns violation details

#### 7. IllegalArgumentException Handler
- Status: 400 BAD_REQUEST
- Logs warning
- Returns error response

#### 8. NullPointerException Handler
- Status: 500 INTERNAL_SERVER_ERROR
- Logs error with stack trace
- Returns generic error response

#### 9. NoHandlerFoundException Handler
- Status: 404 NOT_FOUND
- Logs resource not found
- Returns endpoint not found error

#### 10. RuntimeException Handler
- Status: 500 INTERNAL_SERVER_ERROR
- Logs error with stack trace
- Returns generic error response

#### 11. Generic Exception Handler (Catch-all)
- Status: 500 INTERNAL_SERVER_ERROR
- Logs error with stack trace
- Returns generic error response

## Best Practices

### 1. Use Specific Exceptions
```java
// ✅ Good
throw new ProductNotFoundException(productId);

// ❌ Avoid
throw new RuntimeException("Product not found");
```

### 2. Provide Meaningful Error Messages
```java
// ✅ Good
throw new ProductValidationException("price", "Price must be greater than 0");

// ❌ Avoid
throw new ProductValidationException("Invalid price");
```

### 3. Include Cause for Debugging
```java
// ✅ Good
try {
    // database operation
} catch (DataAccessException e) {
    throw new DatabaseOperationException("save product", e);
}

// ❌ Avoid
throw new RuntimeException("Database error");
```

### 4. Logging Levels
- **DEBUG:** Low-level entry/exit information
- **INFO:** Business-significant events (product saved, updated)
- **WARN:** Recoverable issues (validation failures, not found)
- **ERROR:** Unrecoverable errors (database failures, unexpected exceptions)

### 5. Validation Flow
```
1. @Valid annotation validates at controller level
   └─> MethodArgumentNotValidException (if validation fails)
   
2. Controller/Service method logic validation
   └─> ProductValidationException
   
3. Resource lookup
   └─> ProductNotFoundException
   
4. Database operation
   └─> DatabaseOperationException
```

## Error Response Examples

### Product Not Found (404)
```json
{
  "success": false,
  "message": "Product not found with id: 999",
  "errorCode": "PRODUCT_NOT_FOUND",
  "status": 404,
  "path": "/products/999",
  "timestamp": "2026-03-02T10:30:00"
}
```

### Validation Error (400)
```json
{
  "success": false,
  "message": "Validation failed for one or more fields",
  "errorCode": "VALIDATION_ERROR",
  "status": 400,
  "path": "/products",
  "details": {
    "title": "must not be blank",
    "price": "must be greater than 0"
  },
  "timestamp": "2026-03-02T10:30:00"
}
```

### Database Error (500)
```json
{
  "success": false,
  "message": "Database operation 'save product' failed: Connection timeout",
  "errorCode": "DATABASE_ERROR",
  "status": 500,
  "path": "/products",
  "timestamp": "2026-03-02T10:30:00"
}
```

## Integration Points

### Service Layer
Services throw appropriate exceptions which are caught and re-thrown by the controller

### Controller Layer
Controllers catch exceptions and let GlobalExceptionHandler format the response

### GlobalExceptionHandler
Intercepts all exceptions and returns standardized ErrorResponse

### Client
Client receives structured error response with actionable error codes

## Benefits

1. **Consistency:** All errors follow the same format
2. **Debugging:** Detailed logging at appropriate levels
3. **User-Friendly:** Clear, actionable error messages
4. **Automation:** Error codes enable client-side automation
5. **Testability:** Easy to test exception scenarios
6. **Maintainability:** Centralized exception handling reduces code duplication
7. **Monitoring:** Structured errors enable automated monitoring and alerting

## Testing Exception Handling

### Example Test Cases
```java
@Test
public void testProductNotFound() {
    // Expect ProductNotFoundException
    // Verify HTTP 404 response
    // Verify error code = PRODUCT_NOT_FOUND
}

@Test
public void testInvalidPrice() {
    // Expect ProductValidationException
    // Verify HTTP 400 response
    // Verify field-level validation error details
}

@Test
public void testDatabaseError() {
    // Mock database failure
    // Expect DatabaseOperationException
    // Verify HTTP 500 response
}
```

## Future Enhancements

1. **Error Code Documentation:** Maintain comprehensive error code registry
2. **Internationalization:** Multi-language error messages
3. **Error Tracking:** Integration with error tracking services (Sentry, etc.)
4. **Rate Limiting:** Add rate limiting exception handler
5. **Audit Trail:** Record all exceptions for audit purposes
6. **Circuit Breaker:** Handle external service failures gracefully

