package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.CheckoutRequestDto;
import com.backend.springjpa.entity.Order;
import com.backend.springjpa.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<ApiResponse<String>> createOrder(@Valid @RequestBody CheckoutRequestDto dto) {
        orderService.checkOut(dto);
        return ResponseEntity.ok(ApiResponse.ok("Success", null,"/createOrder"));
    }

    @PostMapping("/createOrder/raceCondition")
    public ResponseEntity<ApiResponse<String>> createOrderRaceCondition(@Valid @RequestBody CheckoutRequestDto dto) {
        orderService.checkoutRaceCondition(dto);
                return ResponseEntity.ok(ApiResponse.ok("Success", null, "/createOrder/raceCondition"));
    }
}
