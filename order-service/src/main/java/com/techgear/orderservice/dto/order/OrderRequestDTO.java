package com.techgear.orderservice.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Long userId;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private List<OrderItemDTO> orderItemsDtoList;
}