package tn.esprit.microservice.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
@Transactional
public class ProductService implements IProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository repo;
    @Autowired
    private EbayService ebayService;


    @Override
    public Product createProduct(Product product) {
        return repo.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        logger.info("Attempting to fetch all products");
        try {
            List<Product> products = repo.findAll();
            logger.info("Successfully fetched {} products", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Error fetching all products", e);
            throw e;
        }
    }

    @Override
    public List<Product> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> filterByCategory(String category) {
        return repo.findByCategory(category);
    }

    @Override
    public Product updateProduct(Long id, Product updated) {
        Product p = getProduct(id);
        if(updated.getName() != null)
            p.setName(updated.getName());
        if(updated.getDescription() != null)
            p.setDescription(updated.getDescription());
        if(updated.getPrice() != 0)
            p.setPrice(updated.getPrice());
        if(updated.getCategory() != null)
            p.setStock(updated.getStock());
        if(updated.getImages() != null)
            p.setImages(updated.getImages());
        if(updated.getName() != null)
            p.setCategory(updated.getCategory());
        return repo.save(p);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }


    @Override
    public ComparisonResult compareWithExternalSources(String productName) {
        Product myProduct = repo.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        List<ComparisonProduct> ebayProducts = ebayService.fetchProducts(productName);

        return new ComparisonResult(myProduct, ebayProducts);
    }

}

