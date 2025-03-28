package com.esprit.microservice.Delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByTrackingNumberContainingIgnoreCase(String trackingNumber);
    List<Delivery> findByStatusContainingIgnoreCase(String status);
}