package com.techgear.orderservice.repositories;

import com.techgear.orderservice.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItems,Long> {
    List<OrderItems> findByOrderId(Long orderId);
}
