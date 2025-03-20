package com.techgear.orderservice.dto.product;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.techgear.orderservice.dto.order.OrderDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;


        @JsonProperty("order")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private OrderDTO orderDto;

}
