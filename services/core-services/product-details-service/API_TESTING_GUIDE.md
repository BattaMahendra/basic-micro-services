# API Testing Guide for Product Service

This guide provides curl commands to test the Product Service API with comprehensive exception handling scenarios.

## Base URL
```
http://localhost:8080
```

## 1. CREATE Product (POST /products)

### Success Scenario - HTTP 201 CREATED
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "title": "MacBook Pro",
    "price": 1299.99,
    "category": "electronics",
    "description": "Powerful laptop for professionals",
    "image": "https://example.com/macbook.jpg"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "title": "MacBook Pro",
    "price": 1299.99,
    "category": "electronics",
    "description": "Powerful laptop for professionals",
    "image": "https://example.com/macbook.jpg",
    "createdAt": "2026-03-02T10:30:00",
    "updatedAt": "2026-03-02T10:30:00"
  },
  "timestamp": "2026-03-02T10:30:00"
}
```

### Error Scenario 1 - Invalid Price (Negative)
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Laptop",
    "price": -100,
    "category": "electronics"
  }'
```

**Expected Response (HTTP 400):**
```json
{
  "success": false,
  "message": "Validation failed for field 'price': Product price must be a positive number",
  "errorCode": "VALIDATION_ERROR",
  "status": 400,
  "timestamp": "2026-03-02T10:30:00"
}
```

### Error Scenario 2 - Empty Title
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "price": 999.99,
    "category": "electronics"
  }'
```

**Expected Response (HTTP 400):**
```json
{
  "success": false,
  "message": "Validation failed for field 'title': Product title cannot be null or empty",
  "errorCode": "VALIDATION_ERROR",
  "status": 400,
  "timestamp": "2026-03-02T10:30:00"
}
```

### Error Scenario 3 - Empty Category
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Laptop",
    "price": 999.99,
    "category": ""
  }'
```

**Expected Response (HTTP 400):**
```json
{
  "success": false,
  "message": "Validation failed for field 'category': Product category cannot be null or empty",
  "errorCode": "VALIDATION_ERROR",
  "status": 400,
  "timestamp": "2026-03-02T10:30:00"
}
```

### Error Scenario 4 - Missing Required Fields
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Laptop"
  }'
```

**Expected Response (HTTP 400):**
```json
{
  "success": false,
  "message": "Validation failed for one or more fields",
  "errorCode": "VALIDATION_ERROR",
  "status": 400,
  "details": {
    "price": "must not be null",
    "category": "must not be blank"
  },
  "timestamp": "2026-03-02T10:30:00"
}
```

## 2. GET All Products (GET /products)

### Success Scenario - HTTP 200 OK
```bash
curl -X GET http://localhost:8080/products
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "MacBook Pro",
      "price": 1299.99,
      "category": "electronics",
      "description": "Powerful laptop"
    },
    {
      "id": 2,
      "title": "iPhone 15",
      "price": 999.99,
      "category": "electronics",
      "description": "Latest smartphone"
    }
  ],
  "timestamp": "2026-03-02T10:30:00"
}
```

## 3. GET Product by ID (GET /products/{id})

### Success Scenario - HTTP 200 OK
```bash
curl -X GET http://localhost:8080/products/1
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "title": "MacBook Pro",
    "price": 1299.99,
    "category": "electronics",
    "description": "Powerful laptop"
  },
  "timestamp": "2026-03-02T10:30:00"
}
```

### Error Scenario 1 - Product Not Found (HTTP 404)
```bash
curl -X GET http://localhost:8080/products/999
```

**Expected Response:**
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

### Error Scenario 2 - Invalid ID (Negative)
```bash
curl -X GET http://localhost:8080/products/-1
```

**Expected Response (HTTP 400):**
```json
{
  "success": false,
  "message": "Product ID must be positive",
  "errorCode": "VALIDATION_ERROR",
  "status": 400,
  "timestamp": "2026-03-02T10:30:00"
}
```

## 4. UPDATE Product (PUT /products/{id})

### Success Scenario - HTTP 200 OK
```bash
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "MacBook Air",
    "price": 1199.99,
    "category": "electronics",
    "description": "Lightweight laptop for professionals"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Product updated successfully",
  "data": {
    "id": 1,
    "title": "MacBook Air",
    "price": 1199.99,
    "category": "electronics",
    "description": "Lightweight laptop for professionals"
  },
  "timestamp": "2026-03-02T10:30:00"
}
```

### Error Scenario 1 - Product Not Found (HTTP 404)
```bash
curl -X PUT http://localhost:8080/products/999 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Laptop",
    "price": 999.99,
    "category": "electronics"
  }'
```

**Expected Response:**
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

### Error Scenario 2 - Invalid Update Data
```bash
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Laptop",
    "price": -100,
    "category": "electronics"
  }'
```

**Expected Response (HTTP 400):**
```json
{
  "success": false,
  "message": "Validation failed for field 'price': Product price must be a positive number",
  "errorCode": "VALIDATION_ERROR",
  "status": 400,
  "timestamp": "2026-03-02T10:30:00"
}
```

## 5. DELETE Product (DELETE /products/{id})

### Success Scenario - HTTP 204 NO_CONTENT
```bash
curl -X DELETE http://localhost:8080/products/1
```

**Expected Response:** (Empty body, just status 204)

### Error Scenario - Product Not Found (HTTP 404)
```bash
curl -X DELETE http://localhost:8080/products/999
```

**Expected Response:**
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

## Error Code Reference

| Error Code | HTTP Status | Meaning |
|-----------|------------|---------|
| VALIDATION_ERROR | 400 | Input validation failed |
| PRODUCT_NOT_FOUND | 404 | Product resource not found |
| DATABASE_ERROR | 500 | Database operation failed |
| CONSTRAINT_VIOLATION | 400 | Constraint violation |
| INVALID_ARGUMENT | 400 | Invalid argument provided |
| NULL_POINTER | 500 | Null reference error |
| ENDPOINT_NOT_FOUND | 404 | API endpoint not found |
| RUNTIME_ERROR | 500 | Unexpected runtime error |
| GENERAL_ERROR | 500 | Unhandled exception |
| INTERNAL_ERROR | 500 | Internal server error |

## Testing Tips

### 1. Pretty Print JSON Response
```bash
curl -X GET http://localhost:8080/products | jq '.'
```

### 2. Show Response Headers
```bash
curl -X GET http://localhost:8080/products -i
```

### 3. Include Request Headers
```bash
curl -X GET http://localhost:8080/products -v
```

### 4. Save Response to File
```bash
curl -X GET http://localhost:8080/products > response.json
```

### 5. Set Custom Headers
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer token" \
  -d '{...}'
```

### 6. Test with Variables
```bash
BASE_URL="http://localhost:8080"
PRODUCT_ID=1

curl -X GET $BASE_URL/products/$PRODUCT_ID
```

## Postman Collection

You can also import this as a Postman collection by creating a new Collection with the endpoints above.

### Quick Import
1. Open Postman
2. Click "Import"
3. Paste the URL: `http://localhost:8080`
4. Add each endpoint manually or use the provided curl commands

## Common Issues & Solutions

### Issue: Connection Refused
**Solution:** Ensure the service is running on port 8080
```bash
# Check if service is running
lsof -i :8080

# Or on Windows:
netstat -ano | findstr :8080
```

### Issue: Malformed JSON
**Solution:** Escape quotes properly in curl commands
```bash
# Windows cmd.exe:
curl -X POST http://localhost:8080/products ^
  -H "Content-Type: application/json" ^
  -d "{\\"title\\": \\"Laptop\\", \\"price\\": 999.99}"

# PowerShell:
$body = @{title='Laptop'; price=999.99; category='electronics'} | ConvertTo-Json
curl -X POST http://localhost:8080/products `
  -H "Content-Type: application/json" `
  -d $body
```

### Issue: 415 Unsupported Media Type
**Solution:** Always include Content-Type header for POST/PUT requests
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{...}'
```

## Performance Testing

### Load Testing with Apache Bench
```bash
# Run 1000 requests with 10 concurrent connections
ab -n 1000 -c 10 http://localhost:8080/products
```

### Load Testing with wrk
```bash
wrk -t4 -c100 -d30s http://localhost:8080/products
```

## Monitoring & Debugging

### View Logs
```bash
# If running via gradle
./gradlew bootRun --args='--logging.level.root=DEBUG'
```

### Check Application Health
```bash
curl -X GET http://localhost:8080/actuator/health
```

### View Metrics
```bash
curl -X GET http://localhost:8080/actuator/metrics
```

