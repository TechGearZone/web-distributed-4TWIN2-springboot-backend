package com.techgear.orderservice.services;

import com.techgear.orderservice.config.RabbitMQConfig;
import com.techgear.orderservice.dto.StockUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendStockUpdate(StockUpdateMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}
