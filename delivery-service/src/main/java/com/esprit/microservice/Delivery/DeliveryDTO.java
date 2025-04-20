
package com.esprit.microservice.Delivery;

public class DeliveryDTO {
    private Long id;
    private Long orderId;
    private String status;
    private String shippingAddress;
    private String trackingNumber;
    private String estimatedDeliveryDate;
    private Long driverId;

    // Constructeurs
    public DeliveryDTO() {}
    public DeliveryDTO(Long id, Long orderId, String status, String shippingAddress, String trackingNumber,
                       String estimatedDeliveryDate, Long driverId) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.trackingNumber = trackingNumber;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.driverId = driverId;
    }

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
