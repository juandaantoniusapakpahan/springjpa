package com.backend.springjpa.mapper;

import com.backend.springjpa.dto.ProductDto;
import com.backend.springjpa.entity.Product;

public class ProductMapper {
    public static ProductDto toProductDTO (Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(String.valueOf(product.getId()));
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setCreatedAt(product.getCreatedAt().toString());
        dto.setUpdatedAt(product.getUpdatedAt().toString());
        return dto;
    }

    public static Product toProduct(ProductDto dto) {
        Product product = new Product();
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        return  product;
    }
}
