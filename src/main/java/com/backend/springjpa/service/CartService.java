package com.backend.springjpa.service;


import com.backend.springjpa.dto.CartDto;
import com.backend.springjpa.entity.Cart;
import com.backend.springjpa.entity.CartItem;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.exception.BadRequestException;
import com.backend.springjpa.repository.CartItemRepository;
import com.backend.springjpa.repository.CartRepository;
import com.backend.springjpa.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductVariantRepository productVariantRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Transactional
    public void upsertCartItem(CartDto cartDto) {
        Cart cart = cartRepository.findByUserId(Long.parseLong(cartDto.getUserId()))
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(Long.parseLong(cartDto.getUserId()));
                    return newCart;
                });

        ProductVariant productVariant = productVariantRepository.findById(Long.parseLong(cartDto.getProductVariantId()))
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        if (productVariant.getStockQty() < Integer.parseInt(cartDto.getQuantity())) {
            throw new BadRequestException("Not enough stock");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), productVariant.getId());
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + Integer.parseInt(cartDto.getQuantity()));
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProductVariant(productVariant);
            newCartItem.setQuantity(Integer.parseInt(cartDto.getQuantity()));
            cartItemRepository.save(newCartItem);
        }
    }
}
