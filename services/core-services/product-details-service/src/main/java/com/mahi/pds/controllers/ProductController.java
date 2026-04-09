package com.mahi.pds.controllers;

import com.mahi.pds.models.ApiResponse;
import com.mahi.pds.exception.ProductException;
import com.mahi.pds.exception.ProductNotFoundException;
import com.mahi.pds.exception.ProductValidationException;
import com.mahi.pds.mapper.ProductMapper;
import com.mahi.pds.models.ProductResponse;
import com.mahi.pds.entity.Product;
import com.mahi.pds.models.ProductRequest;
import com.mahi.pds.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

/**
 * ProductController - REST API endpoint for product operations
 * Provides endpoints for CRUD operations on products
 * All endpoints return standardized ApiResponse wrapper
 */
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    /**
     * Create a new product - Enterprise Grade Implementation
     *
     * Features:
     * - Input validation using JSR-303 annotations
     * - Null/empty checks with meaningful error messages
     * - Object mapping using ProductMapper (separation of concerns)
     * - Service layer validation and transaction management
     * - Comprehensive logging for audit trail
     * - Standard ApiResponse wrapper
     * - HTTP 201 CREATED status code for successful creation
     * - Custom exception handling through GlobalExceptionHandler
     *
     * @param productRequest the product creation request (validated)
     * @return ResponseEntity with ApiResponse containing ProductResponse DTO
     * @throws ProductException if product creation fails
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest productRequest) {

        try {
            // Step 1: Log incoming request
            log.info("Incoming request to create product: title={}, category={}",
                productRequest.getTitle(), productRequest.getCategory());

            // Step 2: Null validation (redundant but defensive with @Valid, but explicit)
            Objects.requireNonNull(productRequest, "ProductRequest must not be null");

            // Step 3: Validate business logic constraints
            if (productRequest.getTitle() == null || productRequest.getTitle().trim().isEmpty()) {
                throw new ProductValidationException("title", "Product title cannot be null or empty");
            }

            if (productRequest.getPrice() == null || productRequest.getPrice() <= 0) {
                throw new ProductValidationException("price", "Product price must be a positive number");
            }

            if (productRequest.getCategory() == null || productRequest.getCategory().trim().isEmpty()) {
                throw new ProductValidationException("category", "Product category cannot be null or empty");
            }

            // Step 4: Map request DTO to entity using mapper
            log.debug("Mapping ProductRequest to Product entity");
            Product productEntity = productMapper.toEntity(productRequest);

            // Step 5: Save product through service layer
            log.info("Saving product to database");
            Product savedProduct = productService.saveProduct(productEntity);

            // Step 6: Map entity to response DTO
            log.debug("Mapping Product entity to ProductResponse DTO");
            ProductResponse productResponse = productMapper.toResponse(savedProduct);

            // Step 7: Return success response with HTTP 201 CREATED
            log.info("Product created successfully with ID: {}", savedProduct.getId());
            ApiResponse<ProductResponse> response = ApiResponse.success(
                productResponse,
                "Product created successfully"
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (ProductException e) {
            // Log product-specific exceptions
            log.warn("Product exception while creating product: {} - Code: {}",
                e.getMessage(), e.getErrorCode());
            throw e;
        } catch (IllegalArgumentException e) {
            // Log validation exceptions
            log.warn("Validation error while creating product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            // Log unexpected exceptions
            log.error("Unexpected error while creating product: {}", e.getMessage(), e);
            throw new ProductException(
                "An unexpected error occurred while creating the product",
                "INTERNAL_ERROR",
                500,
                e
            );
        }
    }

    /**
     * Retrieve all products
     *
     * @return ResponseEntity with list of all products wrapped in ApiResponse
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        try {
            log.info("Fetching all products");
            List<Product> products = productService.getAllProducts();

            ApiResponse<List<Product>> response = ApiResponse.success(
                products,
                "Products retrieved successfully"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching all products: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieve a product by ID
     *
     * @param id the product ID
     * @return ResponseEntity with product data wrapped in ApiResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable @Positive(message = "Product ID must be positive") Long id) {
        try {
            log.info("Fetching product with ID: {}", id);
            Product product = productService.getProductById(id);
            ProductResponse productResponse = productMapper.toResponse(product);

            ApiResponse<ProductResponse> response = ApiResponse.success(
                productResponse,
                "Product retrieved successfully"
            );
            return ResponseEntity.ok(response);
        } catch (ProductException e) {
            log.warn("Error fetching product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching product with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Update an existing product
     *
     * @param id the product ID to update
     * @param productRequest the updated product data
     * @return ResponseEntity with updated product wrapped in ApiResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable @Positive(message = "Product ID must be positive") Long id,
            @Valid @RequestBody ProductRequest productRequest) {
        try {
            Objects.requireNonNull(productRequest, "Product data must not be null");

            log.info("Updating product with ID: {}", id);
            Product updatedProduct = productService.updateProduct(id, productRequest);
            ProductResponse productResponse = productMapper.toResponse(updatedProduct);

            ApiResponse<ProductResponse> response = ApiResponse.success(
                productResponse,
                "Product updated successfully"
            );
            return ResponseEntity.ok(response);
        } catch (ProductException e) {
            log.warn("Error updating product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating product with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Delete a product
     *
     * @param id the product ID to delete
     * @return ResponseEntity with success message wrapped in ApiResponse
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(
            @PathVariable @Positive(message = "Product ID must be positive") Long id) {
        try {
            log.info("Deleting product with ID: {}", id);
            productService.deleteProduct(id);

            ApiResponse<String> response = ApiResponse.success(
                null,
                "Product deleted successfully with ID: " + id
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (ProductException e) {
            log.warn("Error deleting product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting product with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
