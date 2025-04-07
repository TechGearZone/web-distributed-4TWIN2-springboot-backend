package com.techgear.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techgear.orderservice.entities.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Order> orders;
}