package tn.esprit.microservice.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestAPI {

    @Autowired
    private IProductService service;
    @Autowired
<<<<<<< HEAD
    private ProductRepository productRepository;
=======
    private TokenService tokenService;
>>>>>>> 952ec5fb9a8287a5b4ff682233fbeeebc5466ddd

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getOne(@PathVariable Long id) {
        return service.getProduct(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteProduct(id);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return service.search(keyword);
    }

    @GetMapping("/category")
    public List<Product> byCategory(@RequestParam String category) {
        return service.filterByCategory(category);
    }

<<<<<<< HEAD
    @PutMapping("/{id}/reduce-stock")
    public void reduceStock(@PathVariable Long id, @RequestParam int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock to reduce");
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }



=======
    @GetMapping(value = "/compare", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComparisonResult> compareProduct(@RequestParam String name) {
        ComparisonResult result = service.compareWithExternalSources(name);
        return ResponseEntity.ok(result);
    }
>>>>>>> 952ec5fb9a8287a5b4ff682233fbeeebc5466ddd
}

