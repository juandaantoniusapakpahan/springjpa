package com.backend.springjpa.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariantDto {
    private String id;
    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Price is required")
    @Pattern(regexp = "\\d+(\\.\\d+)?", message = "Price must be a number")
    private String price;

    @NotBlank(message = "Stock quantity is required")
    @Pattern(regexp = "\\d+", message = "Stock quantity must be positive integer")
    private String stockQty;

    private String productId;
    private String percentage;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
