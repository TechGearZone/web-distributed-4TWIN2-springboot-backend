package tn.esprit.microservice.productservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tn.esprit.microservice.productservice.Product;
import tn.esprit.microservice.productservice.ProductRepository;
import tn.esprit.microservice.productservice.StockUpdateMessage;

@Component
public class StockUpdateListener {
    private static final Logger logger = LoggerFactory.getLogger(StockUpdateListener.class);

    private final ProductRepository productRepository;

    public StockUpdateListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = "product-stock-update-queue")
    public void handleStockUpdate(StockUpdateMessage message) {
        logger.info("Received stock update: ProductID={}, Quantity={}", message.getProductId(), message.getQuantity());

        try {
            Product product = productRepository.findById(message.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + message.getProductId()));

            if (product.getStock() < message.getQuantity()) {
                logger.error("Insufficient stock for product ID: {}. Available: {}, Requested: {}",
                        message.getProductId(), product.getStock(), message.getQuantity());
                // You might want to send a notification or handle this error differently
                // For now, we'll just log it and not update the stock
                return;
            }

            // Reduce stock by the purchased quantity
            product.setStock(product.getStock() - message.getQuantity());
            productRepository.save(product);

            logger.info("Stock updated for product ID: {}. New stock level: {}", product.getId(), product.getStock());
        } catch (Exception e) {
            logger.error("Error processing stock update: {}", e.getMessage(), e);
        }
    }
}