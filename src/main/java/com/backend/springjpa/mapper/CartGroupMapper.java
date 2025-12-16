package com.backend.springjpa.mapper;

import com.backend.springjpa.dto.CartGroupDto;
import com.backend.springjpa.entity.Product;

public class CartGroupMapper {

    public static CartGroupDto toCartGroupDto(Product product){
        CartGroupDto cartGroupDto = new CartGroupDto();
        cartGroupDto.setId(product.getId());
        cartGroupDto.setName(product.getName());
        return cartGroupDto;
    }
}
