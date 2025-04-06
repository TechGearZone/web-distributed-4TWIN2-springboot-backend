package com.techgear.orderservice.repositories;


import com.techgear.orderservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order,Long> {



}
