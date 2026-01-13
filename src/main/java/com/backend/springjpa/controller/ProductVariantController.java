package com.backend.springjpa.controller;


import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.ProductVariantAverage;
import com.backend.springjpa.dto.ProductVariantDto;
import com.backend.springjpa.dto.StockRiskReport;
import com.backend.springjpa.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product-variant")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    public ProductVariantController(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @PostMapping("/createProductVariant/{productId}")
    public ResponseEntity<ApiResponse<String>> createProductVariant(@PathVariable String productId, @Valid @RequestBody ProductVariantDto dto) {
        dto.setProductId(productId);
        productVariantService.createProductVariant(dto);
        return ResponseEntity.ok(ApiResponse.ok("Product variant created successfully", null, "/createProductVariant"));
    }

    @PostMapping("/updateProductVariant/{productId}/price")
    public ResponseEntity<ApiResponse<Integer>> updateProductVariantPrice(@PathVariable String productId, @RequestBody ProductVariantDto dto) {
        dto.setProductId(productId);
        Integer updatedRows = productVariantService.updatePriceByPercentage(dto);
        return ResponseEntity.ok(ApiResponse.ok("Product variant price updated successfully", updatedRows, "/updateProductVariant"));
    }


    @PostMapping("/getAll")
    public ResponseEntity<ApiResponse<List<ProductVariantDto>>> getAll(Pageable pageable) {
        return ResponseEntity
                .ok(ApiResponse
                        .ok("Success",
                                productVariantService.getAllProductVariant(pageable),
                                "/getAll"));
    }

    @PostMapping("/stock-risk")
    public ResponseEntity<ApiResponse<List<StockRiskReport>>> getStockRisk(
            @RequestParam(defaultValue = "10") int highRisk,
            @RequestParam(defaultValue = "15") int mediumRisk
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Success", productVariantService.getStockRisk(highRisk, mediumRisk),"/stock-risk"));
    }

    @PostMapping("/review")
    public ResponseEntity<ApiResponse<ProductVariantDto>> getProductVariantAndReview(@RequestParam Long variantId) {
        return ResponseEntity.ok(ApiResponse.ok("Success",productVariantService.getProductVariantAndReview(variantId), "/review"));
    }

    @PostMapping("/rating")
    public ResponseEntity<ApiResponse<ProductVariantAverage>> getProductVariantAvgRate(@RequestParam Long variantId,
                                                                                       @RequestParam LocalDate start,
                                                                                       @RequestParam LocalDate end) {
        return ResponseEntity.ok(ApiResponse.ok("Success", productVariantService.getProductVariantAvgRate(variantId, start, end),"/rating"));
    }
}
