package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface SellerSalesReport {
    Long getSellerId();
    String getSellerName();
    Long getTotalOrder();
    BigDecimal getTotalRevenue();
}
