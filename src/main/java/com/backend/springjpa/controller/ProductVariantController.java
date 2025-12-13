package com.backend.springjpa.controller;


import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.ProductVariantDto;
import com.backend.springjpa.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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




}
