package com.mahi.pds.controllers;

import com.mahi.pds.entity.Product;
import com.mahi.pds.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Create product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() throws InterruptedException {

//        Thread.sleep(7000);
        return productService.getAllProducts();
    }

    // Get product by id
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) throws InterruptedException {

        System.out.println("Incoming request for product with id: "+id);
        if(id == 1){
            throw new RuntimeException();
        }
        return productService.getProductById(id);
    }

    // Update product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) throws InterruptedException {

        //initiating sleep
       // Thread.sleep(7000);
        return productService.updateProduct(id, product);
    }

    // Delete product
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully with id " + id;
    }
}
