package com.backend.springjpa.mapper;

import com.backend.springjpa.dto.SellerDto;
import com.backend.springjpa.entity.Seller;

public class SellerMapper {
    public static Seller toSeller(SellerDto dto) {
        Seller seller = new Seller();
        seller.setName(dto.getName());
        seller.setEmail(dto.getEmail());
        return seller;
    }

    public static SellerDto toSellerDto(Seller entity) {
        SellerDto dto = new SellerDto();
        dto.setId(String.valueOf(entity.getId()));
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setCreatedAt(entity.getCreatedAt().toString());
        return dto;
    }
}
