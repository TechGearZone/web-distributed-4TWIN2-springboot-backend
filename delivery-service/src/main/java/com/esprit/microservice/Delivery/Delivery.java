package com.esprit.microservice.Delivery;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Order ID ne doit pas être null")
    @Column(unique = true)
    private Long orderId;

    @NotBlank(message = "Le statut de la livraison est requis")
    @Pattern(regexp = "^(Pending|Shipped|Delivered|Cancelled)$",
            message = "Le statut doit être 'Pending', 'Shipped', 'Delivered' ou 'Cancelled'")
    private String status;

    @NotBlank(message = "L'adresse de livraison est requise")
    @Size(min = 5, max = 255, message = "L'adresse doit comporter entre 5 et 255 caractères")
    private String shippingAddress;
    private String trackingNumber;
    private String estimatedDeliveryDate;
    private Long driverId; // ID du livreur affecté (null si non affecté)

    // Constructeurs
    public Delivery() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
}