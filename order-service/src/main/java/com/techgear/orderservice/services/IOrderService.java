package com.techgear.orderservice.services;

import com.techgear.orderservice.dto.order.OrderRequestDTO;
import com.techgear.orderservice.entities.Order;

import java.util.List;

public interface IOrderService {

    public List<Order> getAll();

    public Order addOrder(Order c);

    public Order updateOrder(int id, Order newOrder);
    public String deleteOrder(int id);

/*
    public List<Product> getAllProducts();

    public Product getProductyId(int id);

    public void saveFavoriteProduct(int orderId, int ProductId);
    public List<Product> getFavoriteProducts(int orderId);*/

    public void placeOrder(OrderRequestDTO orderRequest);
}
