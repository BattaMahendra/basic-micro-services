package com.mahi.order.webClient;

import com.mahi.order.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ProductServiceWebClient {

    @Autowired
    private WebClient webClient;

    //interacting with product-service using web clint
    // MONO - either get 1 result or 0 results
    public Mono<Product> getProductById(Long id){
        return   webClient.
                get().
                uri("{id}",id).
                retrieve().
                bodyToMono(Product.class);
    }

    // Flux - either 0 or N products stream
    public Flux<Product> getProducts(){
        return   webClient.
                get().
                uri("").
                retrieve().
                bodyToFlux(Product.class)

                //extra settings
                .timeout(Duration.ofSeconds(8)) // Fails if no response in 3 seconds
                .retry(2) // Tries the original call + 2 retries on failure
                .onErrorResume(error -> {
                    // Log the error
                    System.out.println("Could not fetch products. Returning default.");
                    // Return a default/cached product
                    return Flux.just(new Product());
                });
    }

    /**
     * 1.You need to learn where to use webclient and rest template
     * 2. Learn about MONO vs FLUX
     * 3. Learn about subscribe() vs block() methods
     * 4. Learn about extra settings like retries, timeouts, fallbacks
     * 5. Most important - Learn about mapping of multiple MONO or FLUX
     *      like map(), filter(), zip() */
}
