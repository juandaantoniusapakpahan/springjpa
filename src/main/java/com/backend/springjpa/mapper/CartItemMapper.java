package com.backend.springjpa.mapper;

import com.backend.springjpa.dto.CartItemDto;
import com.backend.springjpa.entity.CartItem;
import com.backend.springjpa.entity.ProductVariant;

public class CartItemMapper {

    public static CartItemDto toCartItemDto(CartItem item){
        CartItemDto dto = new CartItemDto();
        dto.setId(String.valueOf(item.));
        dto.setName(item.getProductVariant().getProduct().getName());
        dto.setQuantity(item.getQuantity().toString());

    }
}
