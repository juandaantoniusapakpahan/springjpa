package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface SellerBestSellingProductReport {
    Long getProductId();
    String getProductName();
    Long getTotalQuantity();
    BigDecimal getTotalRevenue();
}
