package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.ProductDto;
import com.backend.springjpa.service.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/v1/product")
public class ProductController {

    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/addProduct/{sellerId}")
    public ResponseEntity<ApiResponse<String>> createProduct(@PathVariable Long sellerId, @Valid @RequestBody ProductDto dto) {
        dto.setSellerId(sellerId.toString());
        productService.createProduct(dto);
        return ResponseEntity.ok(ApiResponse.ok("Product added successfully",null, "/addProduct"));
    }

    @PostMapping("/getProductBySellerId")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getBySellerId(@RequestParam Long sellerId,
                                                                       @RequestParam int page,
                                                                       @RequestParam int size) {
        List<ProductDto> productDtos = productService.getProductsBySellerId(page,size,sellerId);
        return ResponseEntity.ok(ApiResponse.ok("Success",productDtos, "/getProductBySellerId"));
    }

    @PostMapping("/getProductsByCategory")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(@RequestParam String category) {
        List<ProductDto> productDtos = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.ok("Success",productDtos, "/getProductsByCategory"));
    }
}
