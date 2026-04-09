package com.mahi.pds.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahi.pds.entity.Product;
import com.mahi.pds.exception.ProductNotFoundException;
import com.mahi.pds.exception.ProductValidationException;
import com.mahi.pds.mapper.ProductMapper;
import com.mahi.pds.models.ProductRequest;
import com.mahi.pds.models.ProductResponse;
import com.mahi.pds.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ProductController with exception handling
 */
@WebMvcTest(ProductController.class)
@DisplayName("Product Controller Exception Handling Tests")
class ProductControllerExceptionTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.boot.test.mock.mockito.MockBean
    private ProductService productService;

    @org.springframework.boot.test.mock.mockito.MockBean
    private ProductMapper productMapper;

    private Product mockProduct;
    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        mockProduct = new Product(1L, "Laptop", 999.99, "High performance", "electronics", "image.jpg");
        productRequest = new ProductRequest();
        productRequest.setTitle("Laptop");
        productRequest.setPrice(999.99);
        productRequest.setCategory("electronics");
        productRequest.setDescription("High performance");

        productResponse = new ProductResponse(1L, "Laptop", 999.99, "High performance", "electronics", "image.jpg");
    }

    @Test
    @DisplayName("Should return 201 CREATED when product created successfully")
    void testCreateProductSuccess() throws Exception {
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(mockProduct);
        when(productService.saveProduct(any(Product.class))).thenReturn(mockProduct);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Laptop"));
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST with validation error when price is negative")
    void testCreateProductWithNegativePrice() throws Exception {
        ProductRequest invalidRequest = new ProductRequest();
        invalidRequest.setTitle("Laptop");
        invalidRequest.setPrice(-100.0);
        invalidRequest.setCategory("electronics");

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 NOT_FOUND when product not found")
    void testGetProductNotFound() throws Exception {
        when(productService.getProductById(999L))
                .thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should return 200 OK when getting product successfully")
    void testGetProductSuccess() throws Exception {
        when(productService.getProductById(1L)).thenReturn(mockProduct);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Laptop"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent product")
    void testUpdateProductNotFound() throws Exception {
        when(productService.updateProduct(eq(999L), any(ProductRequest.class)))
                .thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(put("/products/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    @DisplayName("Should return 200 OK when updating product successfully")
    void testUpdateProductSuccess() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(mockProduct);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product updated successfully"));
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent product")
    void testDeleteProductNotFound() throws Exception {
        doThrow(new ProductNotFoundException(999L))
                .when(productService).deleteProduct(999L);

        mockMvc.perform(delete("/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    @DisplayName("Should return 204 NO_CONTENT when deleting product successfully")
    void testDeleteProductSuccess() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST when product ID is not positive")
    void testGetProductWithNegativeId() throws Exception {
        mockMvc.perform(get("/products/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 200 OK when getting all products")
    void testGetAllProductsSuccess() throws Exception {
        when(productService.getAllProducts()).thenReturn(java.util.Arrays.asList(mockProduct));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Products retrieved successfully"));
    }
}

