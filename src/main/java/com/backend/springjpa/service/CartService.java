package com.backend.springjpa.service;


import com.backend.springjpa.dto.CartDto;
import com.backend.springjpa.dto.ProductDto;
import com.backend.springjpa.dto.ProductVariantDto;
import com.backend.springjpa.entity.Cart;
import com.backend.springjpa.entity.CartItem;
import com.backend.springjpa.entity.Product;
import com.backend.springjpa.entity.ProductVariant;
import com.backend.springjpa.exception.BadRequestException;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.mapper.ProductMapper;
import com.backend.springjpa.mapper.ProductVariantMapper;
import com.backend.springjpa.repository.CartItemRepository;
import com.backend.springjpa.repository.CartRepository;
import com.backend.springjpa.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public void addCartItem(CartDto cartDto) {
        Cart cart = cartRepository.findByUserId(Long.parseLong(cartDto.getUserId()))
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(Long.parseLong(cartDto.getUserId()));
                    return cartRepository.save(newCart);
                });
        ProductVariant productVariant = productVariantRepository.findById(Long.parseLong(cartDto.getProductVariantId()))
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        CartItem existingItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), productVariant.getId())
                .orElseGet(()->{
                    CartItem newCartItem = new CartItem();
                    newCartItem.setCart(cart);
                    newCartItem.setProductVariant(productVariant);
                    newCartItem.setQuantity(0);
                    return newCartItem;
                });
        existingItem.setQuantity(existingItem.getQuantity() + Integer.parseInt(cartDto.getQuantity()));

        if (existingItem.getQuantity() > productVariant.getStockQty()) {
            throw new BadRequestException("Not enough stock");
        }
        cartItemRepository.save(existingItem);
    }

    @Transactional
    public void reduceCartItem(CartDto dto) {
        Cart cart = cartRepository.findByUserId(Long.parseLong(dto.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item =cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), Long.parseLong(dto.getProductVariantId()))
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        Integer reducedAmount = Integer.parseInt(dto.getQuantity());
        if (item.getQuantity()-reducedAmount <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(item.getQuantity() - reducedAmount);
            cartItemRepository.save(item);
        }
    }

    public List<ProductDto> getCartGroupedByCategory(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found"));
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        Map<Long, ProductDto> map = new LinkedHashMap<>();

        for (CartItem ci: cartItems) {
            Product product = ci.getProductVariant().getProduct();
            ProductDto productDto = map.computeIfAbsent(
                    product.getId(),
                    id-> ProductMapper.toProductDTO(product));
            ProductVariantDto productVariantDto = ProductVariantMapper.toProductVariantDto(ci.getProductVariant());
            productDto.getVariants().add(productVariantDto);
        }
        return new ArrayList<>(map.values());
    }
}
