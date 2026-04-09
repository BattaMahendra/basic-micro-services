package com.mahi.pds.services;

import com.mahi.pds.entity.Product;
import com.mahi.pds.exception.DatabaseOperationException;
import com.mahi.pds.exception.ProductNotFoundException;
import com.mahi.pds.exception.ProductValidationException;
import com.mahi.pds.models.ProductRequest;
import com.mahi.pds.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductService exception handling
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Exception Handling Tests")
class ProductServiceExceptionTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        mockProduct = new Product(1L, "Laptop", 999.99, "High performance", "electronics", "image.jpg");
        productRequest = new ProductRequest();
        productRequest.setTitle("Laptop");
        productRequest.setPrice(999.99);
        productRequest.setCategory("electronics");
    }

    @Test
    @DisplayName("Should throw ProductValidationException when title is empty")
    void testSaveProductWithEmptyTitle() {
        Product product = new Product();
        product.setTitle("");
        product.setPrice(100.0);
        product.setCategory("electronics");

        assertThrows(ProductValidationException.class, () -> productService.saveProduct(product));
    }

    @Test
    @DisplayName("Should throw ProductValidationException when price is negative")
    void testSaveProductWithNegativePrice() {
        Product product = new Product();
        product.setTitle("Laptop");
        product.setPrice(-100.0);
        product.setCategory("electronics");

        assertThrows(ProductValidationException.class, () -> productService.saveProduct(product));
    }

    @Test
    @DisplayName("Should throw ProductValidationException when category is empty")
    void testSaveProductWithEmptyCategory() {
        Product product = new Product();
        product.setTitle("Laptop");
        product.setPrice(999.99);
        product.setCategory("");

        assertThrows(ProductValidationException.class, () -> productService.saveProduct(product));
    }

    @Test
    @DisplayName("Should save product successfully when data is valid")
    void testSaveProductSuccess() {
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        Product savedProduct = productService.saveProduct(mockProduct);

        assertNotNull(savedProduct);
        assertEquals("Laptop", savedProduct.getTitle());
        assertEquals(999.99, savedProduct.getPrice());
        verify(productRepository, times(1)).save(mockProduct);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found by ID")
    void testGetProductByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    @DisplayName("Should retrieve product successfully when found")
    void testGetProductByIdSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Product product = productService.getProductById(1L);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getTitle());
    }

    @Test
    @DisplayName("Should throw ProductValidationException when ID is negative")
    void testGetProductByNegativeId() {
        assertThrows(ProductValidationException.class, () -> productService.getProductById(-1L));
    }

    @Test
    @DisplayName("Should throw ProductValidationException when ID is zero")
    void testGetProductByZeroId() {
        assertThrows(ProductValidationException.class, () -> productService.getProductById(0L));
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when database fails on save")
    void testSaveProductDatabaseError() {
        when(productRepository.save(any(Product.class)))
                .thenThrow(new RuntimeException("Connection timeout"));

        assertThrows(DatabaseOperationException.class, () -> productService.saveProduct(mockProduct));
    }

    @Test
    @DisplayName("Should throw DatabaseOperationException when database fails on update")
    void testUpdateProductDatabaseError() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class)))
                .thenThrow(new RuntimeException("Connection timeout"));

        assertThrows(DatabaseOperationException.class,
                () -> productService.updateProduct(1L, productRequest));
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProductSuccess() {
        Product updatedProduct = new Product(1L, "Desktop", 1299.99, "Gaming PC", "electronics", "image.jpg");
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setTitle("Desktop");
        updateRequest.setPrice(1299.99);
        updateRequest.setCategory("electronics");
        updateRequest.setDescription("Gaming PC");

        Product result = productService.updateProduct(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Desktop", result.getTitle());
        assertEquals(1299.99, result.getPrice());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when deleting non-existent product")
    void testDeleteProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(999L));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteProductSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).delete(mockProduct);
    }

    @Test
    @DisplayName("Should retrieve all products successfully")
    void testGetAllProductsSuccess() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(mockProduct));

        var products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Laptop", products.get(0).getTitle());
    }
}

