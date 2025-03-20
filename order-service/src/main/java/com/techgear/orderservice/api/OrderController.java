package com.techgear.orderservice.api;


import com.techgear.orderservice.dto.order.OrderRequestDTO;
import com.techgear.orderservice.services.IOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OrderController", description = "Operations related to orders")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final IOrderService orderService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String PlaceOrder(@RequestBody OrderRequestDTO orderRequest){
        orderService.placeOrder(orderRequest);
        return "order placed succesfully";
    }
}
