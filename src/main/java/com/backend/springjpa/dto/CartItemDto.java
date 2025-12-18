package com.backend.springjpa.dto;


import com.backend.springjpa.entity.ProductVariant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {

    private String id;
    private String priceAmount;
    private String quantity;

    @JsonProperty("product_variant")
    private ProductVariantDto productVariantDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(String priceAmount) {
        this.priceAmount = priceAmount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public ProductVariantDto getProductVariantDto() {
        return productVariantDto;
    }

    public void setProductVariantDto(ProductVariantDto productVariantDto) {
        this.productVariantDto = productVariantDto;
    }
}
