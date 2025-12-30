package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.CheckoutRequestDto;
import com.backend.springjpa.dto.DailySalesReportDto;
import com.backend.springjpa.dto.SellerSalesReport;
import com.backend.springjpa.dto.TopProductVariantReport;
import com.backend.springjpa.entity.Order;
import com.backend.springjpa.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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

    @PostMapping("/getDailySalesReport")
    public ResponseEntity<ApiResponse<List<DailySalesReportDto>>> getDailySalesReport(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok("Success", orderService.getDailySalesReport(start, end), "/getDailySalesReport"));
    }

    @PostMapping("/sales/seller")
    public ResponseEntity<ApiResponse<List<SellerSalesReport>>> getSellerSales(@RequestParam LocalDate start, @RequestParam LocalDate end){
        return ResponseEntity.ok(ApiResponse.ok("Success", orderService.getSalesPerSeller(start, end), "/sales/seller"));
    }

    @PostMapping("/getTopSellingVariants")
    public ResponseEntity<ApiResponse<List<TopProductVariantReport>>> getTopSellingVariants(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok("Success", orderService.getTopSellingVariants(start, end), "/getTopSellingVariants"));
    }
}
