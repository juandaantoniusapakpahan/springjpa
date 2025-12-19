package com.backend.springjpa.controller;


import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.PaymentDto;
import com.backend.springjpa.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/createPayment")
    public ResponseEntity<ApiResponse<String>> createPayment(@Valid @RequestBody PaymentDto dto) {
        paymentService.createPayment(dto);
        return ResponseEntity.ok(ApiResponse.ok("Success", null, "/createPayment"));
    }

}
