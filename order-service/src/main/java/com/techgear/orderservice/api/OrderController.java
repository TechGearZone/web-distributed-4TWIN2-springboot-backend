package com.techgear.orderservice.api;


import com.techgear.orderservice.dto.order.OrderItemDTO;
import com.techgear.orderservice.dto.order.OrderRequestDTO;
import com.techgear.orderservice.dto.order.OrderResponseDTO;
import com.techgear.orderservice.entities.Order;
import com.techgear.orderservice.entities.OrderStatus;
import com.techgear.orderservice.services.IOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "OrderController", description = "Operations related to orders")
@RestController
@Slf4j

@RequestMapping("/api/orders")
public class OrderController {
    private final IOrderService orderService;
    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequest) {
        Order order = orderService.placeOrder(orderRequest);
        OrderResponseDTO responseDTO = mapToOrderResponseDTO(order);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderService.getAll();
        List<OrderResponseDTO> responseDTOs = orders.stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));
        OrderResponseDTO responseDTO = mapToOrderResponseDTO(order);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponseDTO> responseDTOs = orders.stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        OrderResponseDTO responseDTO = mapToOrderResponseDTO(updatedOrder);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getOrderItemsList().stream()
                .map(item -> new OrderItemDTO(
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity().intValue(), // Convert BigDecimal to Integer
                        item.getPrice()))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getStatus().toString(),
                order.getUserId(),
                order.getTotalAmount().doubleValue(),
                order.getShippingAddress(),
                order.getBillingAddress(),
                order.getPaymentMethod().toString(),
                itemDTOs
        );
    }
}