package com.esprit.microservice.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired
    private IDeliveryService deliveryService;

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDelivery(@PathVariable Long id) {
        DeliveryDTO delivery = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(delivery);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries(
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) String trackingNumber,
            @RequestParam(required = false) String status) {
        if (trackingNumber != null || status != null) {
            List<DeliveryDTO> deliveries = deliveryService.searchDeliveries(trackingNumber, status);
            return ResponseEntity.ok(deliveries);
        }
        List<DeliveryDTO> deliveries = deliveryService.getAllDeliveries(sortBy);
        return ResponseEntity.ok(deliveries);
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> createDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO createdDelivery = deliveryService.createDelivery(deliveryDTO);
        return ResponseEntity.ok(createdDelivery);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDTO> updateDelivery(@PathVariable Long id, @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO updatedDelivery = deliveryService.updateDelivery(id, deliveryDTO);
        return ResponseEntity.ok(updatedDelivery);
    }

    @PutMapping("/{id}/assign-driver/{driverId}")
    public ResponseEntity<Void> assignDriver(@PathVariable Long id, @PathVariable Long driverId) {
        deliveryService.assignDriver(id, driverId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unassign-driver")
    public ResponseEntity<Void> unassignDriver(@PathVariable Long id) {
        deliveryService.unassignDriver(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.ok().build();
    }
}