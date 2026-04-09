# Exception Handling Architecture - Visual Guide

## 1. Exception Class Hierarchy

```
┌─────────────────────────────────────────────────────────────┐
│                    RuntimeException                         │
│                  (Java Standard)                            │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   ProductException                          │
│                 (Base Custom Exception)                     │
│                                                              │
│  - errorCode: String                                        │
│  - httpStatusCode: int                                      │
│  + getErrorCode(): String                                   │
│  + getHttpStatusCode(): int                                 │
└──────────────────────────┬──────────────────────────────────┘
                           │
          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
   ┌─────────────┐  ┌─────────────────┐  ┌──────────────────┐
   │ProductNot   │  │ProductValidation│  │DatabaseOperation│
   │FoundExc.   │  │Exception        │  │Exception         │
   ├─────────────┤  ├─────────────────┤  ├──────────────────┤
   │ HTTP 404    │  │ HTTP 400        │  │ HTTP 500         │
   │             │  │                 │  │                  │
   │ Error Code: │  │ Error Code:     │  │ Error Code:      │
   │ PRODUCT_NOT │  │ VALIDATION_     │  │ DATABASE_ERROR   │
   │ FOUND       │  │ ERROR           │  │                  │
   └─────────────┘  └─────────────────┘  └──────────────────┘
```

## 2. Request Processing Flow

```
                    ┌─────────────────┐
                    │  HTTP Request   │
                    │  POST /products │
                    └────────┬────────┘
                             │
                             ▼
                   ┌─────────────────────┐
                   │ ProductController   │
                   │                     │
                   │ @PostMapping        │
                   │ createProduct()     │
                   └────────┬────────────┘
                            │
                    ┌───────┴────────┐
                    │                │
                    ▼                ▼
            ┌──────────────┐  ┌────────────────┐
            │ Validation   │  │ Input Mapping  │
            │ @Valid       │  │ ProductMapper  │
            │ Check fields │  │ toEntity()     │
            └──────┬───────┘  └────────┬───────┘
                   │                   │
                   │ ✓ Valid           │
                   └───────┬───────────┘
                           │
                           ▼
                ┌─────────────────────────┐
                │  ProductService         │
                │                         │
                │  saveProduct()          │
                │  • Validate data        │
                │  • Call repository      │
                │  • Log operations       │
                └────────┬────────────────┘
                         │
                    ┌────┴─────┐
                    │           │
                    ▼           ▼
            ┌─────────────┐  ┌───────────┐
            │  Success    │  │ Exception │
            │             │  │           │
            │ Return      │  │ Throw:    │
            │ Product     │  │ ProductEx │
            │ with ID     │  │ ...       │
            └──────┬──────┘  └─────┬─────┘
                   │               │
                   │    ┌──────────┴────────────┐
                   │    │                       │
                   ▼    ▼                       ▼
          ┌──────────────────┐        ┌──────────────────┐
          │ ApiResponse      │        │ GlobalException  │
          │ success: true    │        │ Handler Catches  │
          │ status: 201      │        │ Exception        │
          │ data: Product    │        └────────┬─────────┘
          │                  │                 │
          └────────┬─────────┘                 ▼
                   │                 ┌──────────────────────┐
                   │                 │ Maps to Handler:     │
                   │                 │ - ProductNotFound?   │
                   │                 │   → 404 handler      │
                   │                 │ - Validation?        │
                   │                 │   → 400 handler      │
                   │                 │ - Database?          │
                   │                 │   → 500 handler      │
                   │                 └────────┬─────────────┘
                   │                          │
                   │                          ▼
                   │                 ┌──────────────────────┐
                   │                 │ Creates ErrorResponse│
                   │                 │ success: false       │
                   │                 │ status: 400/404/500  │
                   │                 │ errorCode: CODE      │
                   │                 │ message: Details     │
                   │                 │ path: /products      │
                   │                 │ timestamp: now       │
                   │                 └────────┬─────────────┘
                   │                          │
                   └──────────┬───────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │  HTTP Response       │
                   │  with appropriate    │
                   │  status code &       │
                   │  JSON body           │
                   └──────────────────────┘
```

## 3. GlobalExceptionHandler Flow

```
                   ┌─────────────────────┐
                   │  Exception Thrown   │
                   └──────────┬──────────┘
                              │
                              ▼
                   ┌─────────────────────────────────────┐
                   │    GlobalExceptionHandler           │
                   │    @RestControllerAdvice            │
                   └──────────┬────────────────────────┘
                              │
                    ┌─────────┴──────────┐
                    │ Exception Type?    │
                    └────────┬───────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │ ...
              ▼              ▼              ▼
    ┌──────────────┐  ┌──────────────┐  ┌────────┐
    │ProductNotFnd │  │Validation    │  │Database│
    │Exception     │  │Exception     │  │Error   │
    └────┬─────────┘  └────┬─────────┘  └───┬────┘
         │                 │                 │
         ▼                 ▼                 ▼
    ┌──────────┐  ┌──────────────┐  ┌──────────────┐
    │createErrorR │createErrorR │createErrorR │
    │esponse     │esponse      │esponse     │
    ├──────────┤  ├──────────────┤  ├──────────────┤
    │status:404 │  │status: 400   │  │status: 500   │
    │code:NOT   │  │code: VALIDAT │  │code: DATABASE│
    │_FOUND     │  │_ERROR        │  │_ERROR        │
    └────┬──────┘  └────┬─────────┘  └────┬────────┘
         │              │                  │
         └──────┬───────┴──────────┬───────┘
                │                  │
                ▼                  ▼
        ┌──────────────────────────────┐
        │  Log Exception at proper     │
        │  DEBUG/INFO/WARN/ERROR level │
        └────────────┬─────────────────┘
                     │
                     ▼
        ┌──────────────────────────────┐
        │  Return ResponseEntity       │
        │  with appropriate HTTP       │
        │  status & ErrorResponse body │
        └──────────────────────────────┘
```

## 4. Validation Flow

```
                    ┌──────────────────┐
                    │ Request arrives  │
                    │ with JSON body   │
                    └────────┬─────────┘
                             │
                             ▼
                ┌──────────────────────────┐
                │ @Valid annotation        │
                │ on @RequestBody          │
                │ Triggers JSR-303         │
                │ validation               │
                └────────┬─────────────────┘
                         │
                    ┌────┴──────────┐
                    │               │
        All Valid ✓ │               │ Validation Error ✗
                    │               │
                    ▼               ▼
        ┌──────────────────┐  ┌──────────────────┐
        │ Continue to      │  │MethodArgument    │
        │ Controller       │  │NotValidException │
        │ method           │  │ thrown           │
        │                  │  │                  │
        │ Proceed with     │  │ Caught by:       │
        │ business logic   │  │ GlobalException  │
        │                  │  │ Handler          │
        └────────┬─────────┘  └────────┬─────────┘
                 │                     │
                 ▼                     ▼
        ┌──────────────────┐  ┌──────────────────┐
        │ Service layer    │  │ handleMethodArg  │
        │ validation       │  │ NotValidException│
        │ (additional)     │  │ ()               │
        │                  │  │                  │
        │ Throw specific   │  │ Extracts field   │
        │ exception if     │  │ errors           │
        │ business rules   │  │ Logs warning     │
        │ violated         │  │                  │
        └────────┬─────────┘  └────────┬─────────┘
                 │                     │
                 └────────┬────────────┘
                          │
                          ▼
                 ┌──────────────────────┐
                 │ Return ErrorResponse │
                 │ with details field   │
                 │ containing field     │
                 │ level errors         │
                 └──────────────────────┘
```

## 5. Exception Handling Decision Tree

```
                        ┌─ Exception Thrown ─┐
                        │                    │
                        ▼
                    Is it a custom
                    ProductException?
                    /            \
                  YES              NO
                  /                  \
                 ▼                    ▼
        Check error type?      Is it MethodArgument
        /      |      \        NotValidException?
       /       |       \            /        \
      /        |        \         YES         NO
     ▼         ▼         ▼         ▼          ▼
   NotFnd  Validation Database  Valid?  Continue...
    404     Error 400  Error 500  /
              |          |      /
              ▼          ▼    ▼
           → 400 → 500  → Extract field
                         errors → 400

           Continue checking for:
           - ConstraintViolationException (400)
           - IllegalArgumentException (400)
           - NullPointerException (500)
           - NoHandlerFoundException (404)
           - RuntimeException (500)
           - Generic Exception (500)
```

## 6. API Response Structure by Scenario

### Success Response (201 Created)
```
┌──────────────────────────────────────┐
│ HTTP/1.1 201 CREATED                 │
│ Content-Type: application/json        │
├──────────────────────────────────────┤
│ {                                    │
│   "success": true,                   │
│   "message": "Product created...",   │
│   "data": {                          │
│     "id": 1,                         │
│     "title": "...",                  │
│     "price": 999.99,                 │
│     ...                              │
│   },                                 │
│   "timestamp": "2026-03-02T10:30:00" │
│ }                                    │
└──────────────────────────────────────┘
```

### Validation Error (400)
```
┌──────────────────────────────────────┐
│ HTTP/1.1 400 BAD_REQUEST             │
│ Content-Type: application/json        │
├──────────────────────────────────────┤
│ {                                    │
│   "success": false,                  │
│   "message": "Validation failed...", │
│   "errorCode": "VALIDATION_ERROR",   │
│   "status": 400,                     │
│   "path": "/products",               │
│   "details": {                       │
│     "price": "must be positive",     │
│     "category": "must not be blank"  │
│   },                                 │
│   "timestamp": "2026-03-02T10:30:00" │
│ }                                    │
└──────────────────────────────────────┘
```

### Not Found Error (404)
```
┌──────────────────────────────────────┐
│ HTTP/1.1 404 NOT_FOUND               │
│ Content-Type: application/json        │
├──────────────────────────────────────┤
│ {                                    │
│   "success": false,                  │
│   "message": "Product not found...", │
│   "errorCode": "PRODUCT_NOT_FOUND",  │
│   "status": 404,                     │
│   "path": "/products/999",           │
│   "timestamp": "2026-03-02T10:30:00" │
│ }                                    │
└──────────────────────────────────────┘
```

### Server Error (500)
```
┌──────────────────────────────────────┐
│ HTTP/1.1 500 INTERNAL_SERVER_ERROR   │
│ Content-Type: application/json        │
├──────────────────────────────────────┤
│ {                                    │
│   "success": false,                  │
│   "message": "Database operation...",│
│   "errorCode": "DATABASE_ERROR",     │
│   "status": 500,                     │
│   "path": "/products",               │
│   "timestamp": "2026-03-02T10:30:00" │
│ }                                    │
└──────────────────────────────────────┘
```

## 7. Layer Interaction Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                      API Client                             │
│              (Browser, Postman, Mobile App)                 │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP Request
                           ▼
        ┌──────────────────────────────────────┐
        │      Spring DispatcherServlet        │
        │        Routes requests               │
        └──────────────────┬───────────────────┘
                           │
                           ▼
        ┌──────────────────────────────────────┐
        │   ProductController                  │
        │   • Input validation (@Valid)        │
        │   • Request mapping                  │
        │   • Response formatting              │
        └──────────────────┬───────────────────┘
                           │
                    ┌──────┴───────┐
                    │              │
                    ▼              ▼
        ┌────────────────────┐  ┌──────────────────┐
        │ ProductService     │  │ GlobalException  │
        │ • Business logic   │  │ Handler          │
        │ • Validation       │  │ • Catches excs   │
        │ • Logging          │  │ • Formats errors │
        │ • Transactions     │  │ • Returns response
        └────────────────────┘  └────────┬─────────┘
                │                        │
                ▼                        │
        ┌──────────────────┐             │
        │ ProductRepository│             │
        │ • DB operations  │             │
        └────────────────────┘           │
                │                        │
        ┌───────┘                        │
        │                                │
        ▼                                │
    Database                             │
        │ ◄─── Exception if fails        │
        │                                │
        └────────────────┬───────────────┘
                         │
                         ▼
        ┌──────────────────────────────────┐
        │   ResponseEntity<ApiResponse>    │
        │   or                             │
        │   ResponseEntity<ErrorResponse>  │
        └──────────────────┬───────────────┘
                           │ HTTP Response
                           ▼
        ┌──────────────────────────────────┐
        │      API Client Receives         │
        │      Response with status        │
        │      and JSON body               │
        └──────────────────────────────────┘
```

## 8. Exception Cause Chain

```
User Action
    │
    ├─ Input Validation Error
    │   └─ ProductValidationException
    │       └─ Caught by: handleProductValidationException()
    │           └─ Response: HTTP 400
    │
    ├─ Resource Not Found
    │   └─ ProductNotFoundException
    │       └─ Caught by: handleProductNotFoundException()
    │           └─ Response: HTTP 404
    │
    ├─ Database Operation Error
    │   ├─ DatabaseException (underlying)
    │   └─ DatabaseOperationException
    │       └─ Caught by: handleDatabaseOperationException()
    │           └─ Response: HTTP 500
    │
    └─ Unexpected Error
        ├─ NullPointerException
        │   └─ Caught by: handleNullPointerException()
        │       └─ Response: HTTP 500
        │
        └─ Generic Exception
            └─ Caught by: handleGenericException()
                └─ Response: HTTP 500
```

## 9. Complete Request-Response Lifecycle

```
1. Client sends HTTP request
   └─ POST /products with JSON body
   
2. Spring receives & routes to controller
   └─ ProductController.createProduct()
   
3. Input validation
   ├─ @Valid annotation triggers
   ├─ JSR-303 rules applied
   └─ Controller method validates
   
4. Service layer processing
   ├─ Business logic execution
   ├─ Additional validation
   └─ Database operation
   
5a. SUCCESS PATH:
    ├─ Product saved
    ├─ Return to controller
    ├─ Wrap in ApiResponse
    ├─ Return HTTP 201
    └─ Client receives success response
    
5b. FAILURE PATH:
    ├─ Exception thrown
    ├─ GlobalExceptionHandler catches
    ├─ Appropriate handler executes
    ├─ Log exception
    ├─ Create ErrorResponse
    ├─ Return HTTP error code
    └─ Client receives error response
    
6. Client processes response
   └─ Reads status code & body
```

---

This visual guide helps understand the exception handling architecture and data flow through the system.

