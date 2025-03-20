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


   

}
