package com.mahi.pds.services;

import com.mahi.pds.entity.Product;
import com.mahi.pds.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Read all
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Read by id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Update
    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = getProductById(id);

        existingProduct.setTitle(productDetails.getTitle());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setImage(productDetails.getImage());

        return productRepository.save(existingProduct);
    }

    // Delete
    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        productRepository.delete(existingProduct);
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
            System.out.println("✅ Sample products loaded.");
        }
    }
}
