package com.techgear.orderservice.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Exclude null fields from JSON response
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;
    private Long userId;
    private Double totalAmount;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private List<OrderItemDTO> items;
}