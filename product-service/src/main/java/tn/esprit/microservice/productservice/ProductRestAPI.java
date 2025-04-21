package tn.esprit.microservice.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/products")
public class ProductRestAPI {

    private final IProductService service;
    private final TokenService tokenService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductRestAPI(IProductService service, TokenService tokenService, ProductRepository productRepository) {
        this.service = service;
        this.tokenService = tokenService;
        this.productRepository = productRepository;
    }
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

    @GetMapping(value = "/compare", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComparisonResult> compareProduct(@RequestParam String name) {
        ComparisonResult result = service.compareWithExternalSources(name);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/reduce-stock/{id}")
    public ResponseEntity<String> reduceStock(@PathVariable Long id, @RequestParam int quantity) {
        try {
            service.reduceStock(id, quantity);
            return ResponseEntity.ok("Stock reduced successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error reducing stock");
        }
    }

}