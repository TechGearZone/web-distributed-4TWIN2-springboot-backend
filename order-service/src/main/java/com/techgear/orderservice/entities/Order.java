package com.techgear.orderservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techgear.orderservice.dto.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @ManyToOne // Create the many-to-one relationship with User
    @JoinColumn(name = "user_id", referencedColumnName = "id") // Foreign key column
    private User user;

    private BigDecimal totalAmount;
    private String shippingAddress;
    private String billingAddress;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItems> orderItemsList = new ArrayList<>();

}
