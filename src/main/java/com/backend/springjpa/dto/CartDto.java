package com.backend.springjpa.dto;

import com.backend.springjpa.entity.ProductVariant;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto {

    private String id;
    @NotBlank(message = "Product variant ID is required")
    private String productVariantId;

    @NotBlank(message = "Quantity is required")
    private String quantity;

    @NotBlank(message = "User ID is required")
    private String userId;

    private ProductVariant productVariant;

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }
}
