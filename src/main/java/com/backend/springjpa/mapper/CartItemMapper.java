package com.backend.springjpa.mapper;

import com.backend.springjpa.dto.CartItemDto;
import com.backend.springjpa.entity.CartItem;
import com.backend.springjpa.entity.ProductVariant;

import java.math.BigDecimal;

public class CartItemMapper {

    public static CartItemDto toCartItemDto(CartItem item){
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId().toString());
        dto.setQuantity(item.getQuantity().toString());
        return dto;
    }
}
