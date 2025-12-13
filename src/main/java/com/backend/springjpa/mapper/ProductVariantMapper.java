package com.backend.springjpa.mapper;

import com.backend.springjpa.dto.ProductVariantDto;
import com.backend.springjpa.entity.ProductVariant;

import java.math.BigDecimal;

public class ProductVariantMapper {
    public static ProductVariant toProductVariant(ProductVariantDto dto) {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setSku(dto.getSku());
        productVariant.setPrice(new BigDecimal(dto.getPrice()));
        productVariant.setStockQty(Integer.parseInt(dto.getStockQty()));
        return productVariant;
    }

    public static ProductVariantDto toProductVariantDto(ProductVariant productVariant) {
        ProductVariantDto dto = new ProductVariantDto();
        dto.setId(String.valueOf(productVariant.getId()));
        dto.setSku(productVariant.getSku());
        dto.setPrice(String.valueOf(productVariant.getPrice()));
        dto.setStockQty(String.valueOf(productVariant.getStockQty()));
        dto.setProductId(productVariant.getProduct().getId().toString());
        return dto;
    }
}
