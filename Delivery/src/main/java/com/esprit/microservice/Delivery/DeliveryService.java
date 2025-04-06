package com.esprit.microservice.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService implements IDeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private TwilioConfig twilioConfig;

    @Autowired
    private PhoneNumberResolver phoneNumberResolver;
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
    private void sendTrackingNumberSMS(String phoneNumber, String trackingNumber) {
        try {
            String messageBody = "Your delivery tracking number is: " + trackingNumber;
            Message.creator(
                    new PhoneNumber(phoneNumber), // To number
                    new PhoneNumber(twilioConfig.getTwilioPhoneNumber()), // From number
                    messageBody
            ).create();
            System.out.println("SMS sent successfully to " + phoneNumber);
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
        }
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
        // Send SMS if tracking number is provided
        if (deliveryDTO.getTrackingNumber() != null && deliveryDTO.getOrderId() != null) {
            String phoneNumber = phoneNumberResolver.getPhoneNumberForOrder(deliveryDTO.getOrderId());
            if (phoneNumber != null) {
                sendTrackingNumberSMS(phoneNumber, deliveryDTO.getTrackingNumber());
            }
        }
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
    @Override
    public DeliveryDTO trackDelivery(String trackingNumber) {
        Delivery delivery = deliveryRepository.findByTrackingNumberContainingIgnoreCase(trackingNumber)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with tracking number: " + trackingNumber));
        return mapToDTO(delivery);
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
