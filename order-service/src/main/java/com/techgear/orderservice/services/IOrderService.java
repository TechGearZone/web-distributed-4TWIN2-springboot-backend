package com.techgear.orderservice.services;

import com.techgear.orderservice.dto.Product;
import com.techgear.orderservice.entities.Order;


import java.util.List;


public interface IOrderService {

    Order createOrder(Order order);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
    void deleteOrder(Long id);
    public Order updateOrder(Long id, Order updatedOrder);
    public Product fetchProductDetails(Long productId);
    public List<Order> getUnpaidOrders();
}