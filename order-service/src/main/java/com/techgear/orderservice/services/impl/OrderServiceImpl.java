package com.techgear.orderservice.services.impl;

import com.techgear.orderservice.dto.order.OrderItemDTO;
import com.techgear.orderservice.dto.order.OrderRequestDTO;
import com.techgear.orderservice.entities.Order;
import com.techgear.orderservice.entities.OrderItems;
import com.techgear.orderservice.entities.OrderStatus;
import com.techgear.orderservice.entities.PaymentMethod;
import com.techgear.orderservice.repositories.OrderRepository;
import com.techgear.orderservice.services.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAll() {
        log.info("Fetching all orders");
        return orderRepository.findAll();
    }

    @Override
    public Order addOrder(Order order) {
        log.info("Adding new order");
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(int id, Order newOrder) {
        log.info("Updating order with ID: {}", id);
        return orderRepository.findById((long) id)
                .map(existingOrder -> {
                    existingOrder.setStatus(newOrder.getStatus());
                    existingOrder.setShippingAddress(newOrder.getShippingAddress());
                    existingOrder.setBillingAddress(newOrder.getBillingAddress());
                    existingOrder.setPaymentMethod(newOrder.getPaymentMethod());
                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));
    }

    @Override
    public String deleteOrder(int id) {
        log.info("Deleting order with ID: {}", id);
        if (orderRepository.existsById((long) id)) {
            orderRepository.deleteById((long) id);
            return "Order deleted successfully";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id);
        }
    }

    @Override
    public Order placeOrder(OrderRequestDTO orderRequest) {
        log.info("Placing new order for user: {}", orderRequest.getUserId());
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        order.setUserId(orderRequest.getUserId());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setBillingAddress(orderRequest.getBillingAddress());
        order.setPaymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);

        // Calculate total amount from order items
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItems> orderLineItems = orderRequest.getOrderItemsDtoList()
                .stream()
                .map(itemDto -> {
                    OrderItems orderItem = mapToDto(itemDto);
                    orderItem.setOrder(order); // Set the relationship
                    return orderItem;
                })
                .toList();

        order.setOrderItemsList(orderLineItems);

        // Calculate total amount - fixed to handle Float price
        for (OrderItems item : orderLineItems) {
            // Convert Float price to BigDecimal and multiply by quantity
            BigDecimal itemPrice = BigDecimal.valueOf(item.getPrice());
            totalAmount = totalAmount.add(itemPrice.multiply(item.getQuantity()));
        }
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user: {}", userId);
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order updateOrderStatus(Long id, OrderStatus status) {
        log.info("Updating status to {} for order ID: {}", status, id);
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id);
        }
    }

    private OrderItems mapToDto(OrderItemDTO orderItemsDto) {
        OrderItems orderItems = new OrderItems();
        orderItems.setPrice(orderItemsDto.getPrice());
        orderItems.setQuantity(BigDecimal.valueOf(orderItemsDto.getQuantity()));
        orderItems.setProductName(orderItemsDto.getProductName());
        orderItems.setProductId(orderItemsDto.getProductId());
        return orderItems;
    }
}