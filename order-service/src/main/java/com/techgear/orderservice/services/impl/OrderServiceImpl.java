package com.techgear.orderservice.services.impl;

import com.techgear.orderservice.entities.Order;

import com.techgear.orderservice.repositories.OrderRepository;
import com.techgear.orderservice.services.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        order.getOrderItemsList().forEach(item -> item.setOrder(order)); // set back-reference
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }


}