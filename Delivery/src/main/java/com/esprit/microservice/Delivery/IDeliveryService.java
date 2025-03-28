package com.esprit.microservice.Delivery;

import java.util.List;

public interface IDeliveryService {
    DeliveryDTO getDeliveryById(Long id);
    List<DeliveryDTO> getAllDeliveries(String sortBy);
    List<DeliveryDTO> searchDeliveries(String trackingNumber, String status);
    DeliveryDTO createDelivery(DeliveryDTO deliveryDTO);
    DeliveryDTO updateDelivery(Long id, DeliveryDTO deliveryDTO);
    void assignDriver(Long deliveryId, Long driverId);
    void unassignDriver(Long deliveryId);
    void deleteDelivery(Long id);
}