package com.mahi.pds.services;

import com.mahi.pds.exception.ProductException;
import com.mahi.pds.exception.ProductNotFoundException;
import com.mahi.pds.exception.ProductValidationException;
import com.mahi.pds.exception.DatabaseOperationException;
import com.mahi.pds.entity.Product;
import com.mahi.pds.models.ProductRequest;
import com.mahi.pds.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * ProductService - Business logic layer for product operations
 * Implements service-level validation, logging, and transaction management
 * Follows Spring best practices with @Transactional annotations
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Save a new product to the database
     * Includes validation and logging
     *
     * @param product the product to save
     * @return saved product with generated ID
     * @throws ProductException if product is invalid
     */
    @Transactional
    public Product saveProduct(Product product) {
        Objects.requireNonNull(product, "Product must not be null");

        // Validate product data
        validateProductData(product);

        try {
            log.info("Saving product: title={}, category={}, price={}",
                product.getTitle(), product.getCategory(), product.getPrice());

            Product savedProduct = productRepository.save(product);

            log.info("Product saved successfully with ID: {}", savedProduct.getId());
            return savedProduct;

        } catch (ProductException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error saving product: {}", e.getMessage(), e);
            throw new DatabaseOperationException("save product", e);
        }
    }

    /**
     * Retrieve all products from the database
     *
     * @return list of all products
     */
    public List<Product> getAllProducts() {
        try {
            log.debug("Fetching all products");
            List<Product> products = productRepository.findAll();
            log.info("Retrieved {} products", products.size());
            return products;
        } catch (Exception e) {
            log.error("Error retrieving all products: {}", e.getMessage(), e);
            throw new DatabaseOperationException("fetch all products", e);
        }
    }

    /**
     * Retrieve a product by ID
     *
     * @param id the product ID
     * @return the product with specified ID
     * @throws ProductNotFoundException if product not found
     */
    public Product getProductById(Long id) {
        Objects.requireNonNull(id, "Product ID must not be null");

        if (id <= 0) {
            throw new ProductValidationException("id", "ID must be positive");
        }

        try {
            log.debug("Fetching product with ID: {}", id);
            return productRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Product not found with ID: {}", id);
                        return new ProductNotFoundException(id);
                    });
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving product with ID {}: {}", id, e.getMessage(), e);
            throw new DatabaseOperationException("fetch product", e);
        }
    }

    /**
     * Update an existing product
     *
     * @param id the product ID to update
     * @param productDetails the new product details
     * @return the updated product
     * @throws ProductNotFoundException if product not found
     */
    @Transactional
    public Product updateProduct(Long id, ProductRequest productDetails) {
        Objects.requireNonNull(productDetails, "Product details must not be null");

        try {
            log.info("Updating product with ID: {}", id);
            Product existingProduct = getProductById(id);

            existingProduct.setTitle(productDetails.getTitle());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setCategory(productDetails.getCategory());
            existingProduct.setImage(productDetails.getImage());

            Product updatedProduct = productRepository.save(existingProduct);
            log.info("Product updated successfully with ID: {}", id);
            return updatedProduct;

        } catch (ProductException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating product with ID {}: {}", id, e.getMessage(), e);
            throw new DatabaseOperationException("update product", e);
        }
    }

    /**
     * Delete a product by ID
     *
     * @param id the product ID to delete
     * @throws ProductNotFoundException if product not found
     */
    @Transactional
    public void deleteProduct(Long id) {
        try {
            log.info("Deleting product with ID: {}", id);
            Product existingProduct = getProductById(id);
            productRepository.delete(existingProduct);
            log.info("Product deleted successfully with ID: {}", id);
        } catch (ProductException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting product with ID {}: {}", id, e.getMessage(), e);
            throw new DatabaseOperationException("delete product", e);
        }
    }

    /**
     * Validate product data
     * Ensures all required fields are valid
     *
     * @param product the product to validate
     * @throws ProductValidationException if validation fails
     */
    private void validateProductData(Product product) {
        if (product.getTitle() == null || product.getTitle().trim().isEmpty()) {
            throw new ProductValidationException("title", "Product title is required");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new ProductValidationException("price", "Product price must be greater than 0");
        }

        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new ProductValidationException("category", "Product category is required");
        }
    }

    @PostConstruct
    public void loadData() {
        if (productRepository.count() == 0) {

            Product p1 = new Product(null, "Backpack", 109.95, "Backpack for everyday use", "men's clothing", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png");
            Product p2 = new Product(null, "Slim Fit T-Shirt", 22.30, "Comfortable slim fit t-shirt", "men's clothing", "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png");
            Product p3 = new Product(null, "Cotton Jacket", 55.99, "Casual cotton jacket", "men's clothing", "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_t.png");
            Product p4 = new Product(null, "Gold Bracelet", 695.00, "Legendary gold & silver bracelet", "jewelery", "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_t.png");
            Product p5 = new Product(null, "External Hard Drive", 64.00, "Portable 2TB hard drive", "electronics", "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_t.png");

            productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
            log.info("✅ Sample products loaded.");
        }
    }
}
