package com.esprit.microservice.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService implements IDeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public DeliveryDTO getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
        return mapToDTO(delivery);
    }

    @Override
    public List<DeliveryDTO> getAllDeliveries(String sortBy) {
        Sort sort = Sort.by(sortBy);
        return deliveryRepository.findAll(sort).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryDTO> searchDeliveries(String trackingNumber, String status) {
        if (trackingNumber != null && !trackingNumber.isEmpty()) {
            return deliveryRepository.findByTrackingNumberContainingIgnoreCase(trackingNumber).stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } else if (status != null && !status.isEmpty()) {
            return deliveryRepository.findByStatusContainingIgnoreCase(status).stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }
        return getAllDeliveries("id");
    }

    @Override
    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(deliveryDTO.getOrderId());
        delivery.setStatus(deliveryDTO.getStatus());
        delivery.setShippingAddress(deliveryDTO.getShippingAddress());
        delivery.setTrackingNumber(deliveryDTO.getTrackingNumber());
        delivery.setEstimatedDeliveryDate(deliveryDTO.getEstimatedDeliveryDate());
        delivery.setDriverId(deliveryDTO.getDriverId());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return mapToDTO(savedDelivery);
    }

    @Override
    public DeliveryDTO updateDelivery(Long id, DeliveryDTO deliveryDTO) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
        delivery.setStatus(deliveryDTO.getStatus());
        delivery.setShippingAddress(deliveryDTO.getShippingAddress());
        delivery.setTrackingNumber(deliveryDTO.getTrackingNumber());
        delivery.setEstimatedDeliveryDate(deliveryDTO.getEstimatedDeliveryDate());
        delivery.setDriverId(deliveryDTO.getDriverId());
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return mapToDTO(updatedDelivery);
    }

    @Override
    public void assignDriver(Long deliveryId, Long driverId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException());
        delivery.setDriverId(driverId);
        deliveryRepository.save(delivery);
    }

    @Override
    public void unassignDriver(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException());
        delivery.setDriverId(null);
        deliveryRepository.save(delivery);
    }

    @Override
    public void deleteDelivery(Long id) {
        deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException()); // Check existence before deletion
        deliveryRepository.deleteById(id);
    }

    private DeliveryDTO mapToDTO(Delivery delivery) {
        return new DeliveryDTO(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getStatus(),
                delivery.getShippingAddress(),
                delivery.getTrackingNumber(),
                delivery.getEstimatedDeliveryDate(),
                delivery.getDriverId()
        );
    }
}