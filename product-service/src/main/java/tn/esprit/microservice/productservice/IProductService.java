package tn.esprit.microservice.productservice;

import java.util.List;

public interface IProductService {
    Product createProduct(Product product);
    Product getProduct(Long id);
    List<Product> getAllProducts();
    List<Product> search(String keyword);
    List<Product> filterByCategory(String category);
    Product updateProduct(Long id, Product updated);
    void deleteProduct(Long id);
    public ComparisonResult compareWithExternalSources(String productName);
}

