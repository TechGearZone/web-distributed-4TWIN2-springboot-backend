package tn.esprit.microservice.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestAPI {

    private static final Logger logger = LoggerFactory.getLogger(ProductRestAPI.class);

    @Autowired
    private IProductService service;
    @Autowired
    private TokenService tokenService;
    private ProductRepository productRepository;

    @Value("${welcome.message}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> getAll() {
        logger.info("Received request to get all products");
        try {
            List<Product> products = service.getAllProducts();
            logger.info("Successfully processed get all products request");
            return products;
        } catch (Exception e) {
            logger.error("Error processing get all products request", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Product getOne(@PathVariable Long id) {
        return service.getProduct(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody Product product) {
        return service.createProduct(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.deleteProduct(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> search(@RequestParam String keyword) {
        return service.search(keyword);
    }

    @GetMapping("/category")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Product> byCategory(@RequestParam String category) {
        return service.filterByCategory(category);
    }

    @GetMapping(value = "/compare", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ComparisonResult> compareProduct(@RequestParam String name) {
        ComparisonResult result = service.compareWithExternalSources(name);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/reduce-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public void reduceStock(@PathVariable Long id, @RequestParam int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock to reduce");
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
