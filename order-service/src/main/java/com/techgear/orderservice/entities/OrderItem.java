package com.techgear.orderservice.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private Long productId;  // Foreign key to product service
    private String productName;
    private Integer quantity;
    private Float price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
