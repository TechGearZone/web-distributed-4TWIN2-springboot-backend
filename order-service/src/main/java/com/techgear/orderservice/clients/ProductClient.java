package com.techgear.orderservice.clients;

import com.techgear.orderservice.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", url = "http://localhost:8093")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    Product getProductById(@PathVariable("id") Long id);

    @GetMapping("/api/products")
    List<Product> getAllProducts();

    @PutMapping("/api/products/reduce-stock/{id}")
    ResponseEntity<String> reduceStock(
            @PathVariable("id") Long id,
            @RequestParam("quantity") int quantity
    );
}
