package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.ProductDto;
import com.backend.springjpa.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/v1/product")
public class ProductController {

    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> createProduct(@Valid @RequestBody ProductDto dto) {
        productService.createProduct(dto);
        return ResponseEntity.ok(ApiResponse.ok("Product added successfully",null, "/product/add"));
    }

//    @PostMapping("/getBySellerId")
//    public ResponseEntity<ApiResponse<List<ProductDto>>> getBySellerId()
}
