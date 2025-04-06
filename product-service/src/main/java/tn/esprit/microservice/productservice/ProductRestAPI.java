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
    private TokenService tokenService;

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

    @GetMapping("/get-token")
    public String getToken() {
        return tokenService.getAccessToken();
    }

}

