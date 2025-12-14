package com.backend.springjpa.controller;

import com.backend.springjpa.dto.ApiResponse;
import com.backend.springjpa.dto.CartDto;
import com.backend.springjpa.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/upsertCart/item")
    public ResponseEntity<ApiResponse<String>> upsertCartItem(@Valid @RequestBody CartDto cartDto) {
        cartService.upsertCartItem(cartDto);
        return ResponseEntity.ok(ApiResponse.ok("", "Cart item added successfully", "/upsertCart/item"));
    }
}
