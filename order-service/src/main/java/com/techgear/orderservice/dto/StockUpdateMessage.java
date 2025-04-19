package com.techgear.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdateMessage implements Serializable {
    private Long productId;
    private int quantity;
}