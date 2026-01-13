package com.backend.springjpa.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class ProductVariantDto {
    private String id;
    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Price is required")
    @Pattern(regexp = "\\d+(\\.\\d+)?", message = "Price must be a number")
    private String price;

    @NotBlank(message = "Stock quantity is required")
    @Pattern(regexp = "\\d+", message = "Stock quantity must be positive integer")
    private String stockQty;

    private String productId;
    private String percentage;
    private List<ReviewDto> reviews;
}
