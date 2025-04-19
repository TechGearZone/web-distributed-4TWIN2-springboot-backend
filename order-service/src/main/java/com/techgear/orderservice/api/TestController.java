package com.techgear.orderservice.api;

import com.techgear.orderservice.dto.StockUpdateMessage;
import com.techgear.orderservice.services.RabbitMQSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final RabbitMQSender rabbitMQSender;

    @PostMapping("/send-stock-update")
    public ResponseEntity<String> testStockUpdate(@RequestParam Long productId, @RequestParam int quantity) {
        try {
            StockUpdateMessage message = new StockUpdateMessage(productId, quantity);
            rabbitMQSender.sendStockUpdate(message);
            return ResponseEntity.ok("Stock update message sent for productId: " + productId + ", quantity: " + quantity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending message: " + e.getMessage());
        }
    }
}