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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {


    private final OrderRepository orderRepository;


    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public Order addOrder(Order c) {
        return null;
    }

    @Override
    public Order updateOrder(int id, Order newOrder) {
        return null;
    }

    @Override
    public String deleteOrder(int id) {
        return null;
    }


    @Override
    public void placeOrder(OrderRequestDTO orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        order.setUserId(orderRequest.getUserId());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setBillingAddress(orderRequest.getBillingAddress());
        order.setPaymentMethod(PaymentMethod.valueOf(orderRequest.getPaymentMethod()));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);

        // Calculate total amount from order items
        Long totalAmount = 0L;

        List<OrderItems> orderLineItems = orderRequest.getOrderItemsDtoList()
                .stream()
                .map(itemDto -> {
                    OrderItems orderItem = mapToDto(itemDto);
                    orderItem.setOrder(order); // Set the relationship
                    return orderItem;
                })
                .toList();

        order.setOrderItemsList(orderLineItems);

        // Calculate total amount
        for (OrderItems item : orderLineItems) {
            totalAmount += item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())).longValue();
        }
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);
    }

    private OrderItems mapToDto(OrderItemDTO orderItemsDto) {
        OrderItems orderItems = new OrderItems();
        orderItems.setPrice(orderItemsDto.getPrice());
        orderItems.setQuantity(orderItemsDto.getQuantity());
        orderItems.setProductName(orderItemsDto.getProductName());
        orderItems.setProductId(orderItemsDto.getProductId());
        return orderItems;
    }

}
