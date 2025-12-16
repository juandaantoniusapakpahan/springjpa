package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.CartDto;
import com.backend.springjpa.dto.CartGroupDto;
import com.backend.springjpa.dto.ProductDto;
import com.backend.springjpa.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addCart/item")
    public ResponseEntity<ApiResponse<String>> upsertCartItem(@Valid @RequestBody CartDto dto) {
        cartService.addCartItem(dto);
        return ResponseEntity.ok(ApiResponse.ok( "Cart item added successfully",null, "/addCart/item"));
    }

    @PostMapping("/reduceCart/item")
    public ResponseEntity<ApiResponse<String>> reduceCartItem(@Valid @RequestBody CartDto dto) {
        cartService.reduceCartItem(dto);
        return ResponseEntity.ok(ApiResponse.ok("Cart item reduced successfully", null, "/reduceCart/item"));
    }

    @PostMapping("/getCartGroupedByCategory/{userId}")
    public ResponseEntity<ApiResponse<List<CartGroupDto>>> getCartGroupedByCategory(@PathVariable Long userId) {
        List<CartGroupDto> dto = cartService.getCartGroupedByCategory(userId);
        return ResponseEntity.ok(ApiResponse.ok("Success",dto, "/getCartGroupedByCategory"));
    }
}
