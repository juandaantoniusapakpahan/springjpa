package com.backend.springjpa.controller;

import com.backend.springjpa.dto.*;
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

    @PostMapping("/getUserOrderSummary")
    public ResponseEntity<ApiResponse<UserOrderSummaryReport>> getUserOrderSummary(@RequestParam Long userId, @RequestParam LocalDate start, @RequestParam LocalDate end)
    {
        return ResponseEntity.ok(ApiResponse.ok("Success", orderService.getUserOrderSummary(userId, start, end),"/getUserOrderSummary"));
    }

    @PostMapping("/getMostPurchasedProduct")
    public ResponseEntity<ApiResponse<UserMostPurchasedProductReport>> getUserMostPurchasedProduct(@RequestParam Long userId, @RequestParam LocalDate start, @RequestParam LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok("Success", orderService.getUserMostPurchasedProduct(userId, start, end), "/getMostPurchasedProduct"));
    }

    @PostMapping("/sales/category")
    public ResponseEntity<ApiResponse<List<RevenuePerCategoryReport>>> getRevenuePerCategory (@RequestParam LocalDate start,
                                                                                              @RequestParam LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok("Success", orderService.getRevenuePerCategory(start, end),"/sales/category"));
    }

    @PostMapping("/conversion-rate/daily")
    public ResponseEntity<ApiResponse<List<DailyConversionRateView>>> getDailyConversionRate(
            @RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Success", orderService.getDailyConversionRate(start, end), "/conversion-rate/daily"));
    }

    @PostMapping("/repeat-buyer")
    public ResponseEntity<ApiResponse<RepeatBuyerReport>> getRepeatBuyer(
            @RequestParam ("minOrder") int minOrder,
            @RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Success", orderService.getRepeatBuyerReportWithRate(start, end
        , minOrder),"/repeat-buyer"));
    }
}
