package tn.esprit.microservice.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository repo;

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
    public List<Product> getAllProducts() {
        return repo.findAll();
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
        p.setName(updated.getName());
        p.setDescription(updated.getDescription());
        p.setPrice(updated.getPrice());
        p.setStock(updated.getStock());
        p.setImages(updated.getImages());
        p.setCategory(updated.getCategory());
        return repo.save(p);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}

