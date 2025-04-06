package com.techgear.orderservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    private String productName;
    private BigDecimal price;
    private Long quantity;
}
