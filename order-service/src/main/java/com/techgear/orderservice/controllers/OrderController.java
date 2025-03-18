package com.techgear.orderservice.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name= "order microservice")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
}
