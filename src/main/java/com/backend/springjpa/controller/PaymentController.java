package com.backend.springjpa.controller;


import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.PaymentDto;
import com.backend.springjpa.dto.PaymentMethodSummaryReport;
import com.backend.springjpa.dto.PaymentStatusSummaryReport;
import com.backend.springjpa.service.PaymentService;
import com.backend.springjpa.util.PaymentStatus;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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

    @PostMapping("/getAllPayment")
    public ResponseEntity<ApiResponse<List<PaymentDto>>> getAllPayment(@RequestParam PaymentStatus status, Pageable page) {
        List<PaymentDto> payments = paymentService.getAllPayment(status, page);
        return ResponseEntity.ok(ApiResponse.ok("Success",payments, "/getAllPayment"));
    }

    @PostMapping("/method-summary")
    public ResponseEntity<ApiResponse<List<PaymentMethodSummaryReport>>> getPaymentMethodSummary(
            @RequestParam("start")LocalDate start, @RequestParam("end") LocalDate end){
        return ResponseEntity.ok(ApiResponse.ok("Success", paymentService.getPaymentMethodSummary(start, end),"/method-summary"));
    }

    @PostMapping("/status-summary")
    public ResponseEntity<ApiResponse<List<PaymentStatusSummaryReport>>> getPaymentStatusSummary(
            @RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Success", paymentService.getPaymentStatusSummary(start, end), "/status-summary"));
    }

}
