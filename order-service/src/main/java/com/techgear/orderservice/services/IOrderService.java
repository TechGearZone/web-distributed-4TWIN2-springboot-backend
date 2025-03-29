package com.techgear.orderservice.services;

import com.techgear.orderservice.dto.order.OrderRequestDTO;
import com.techgear.orderservice.entities.Order;
import com.techgear.orderservice.entities.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface IOrderService {

    public List<Order> getAll();

    public Order addOrder(Order c);

    public Order updateOrderStatus(Long id, OrderStatus status);
    public Order updateOrder(int id, Order newOrder);
    public void deleteOrder(Long id);
    public String deleteOrder(int id);
    public Order placeOrder(OrderRequestDTO orderRequest);
    public Optional<Order> getOrderById(Long id);
    public List<Order> getOrdersByUserId(Long userId);
}
