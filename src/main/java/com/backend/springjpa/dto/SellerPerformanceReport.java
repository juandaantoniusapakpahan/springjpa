package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface SellerPerformanceReport {
    Long getSellerId();
    String getSellerName();
    BigDecimal getTotalRevenue();
    Long getTotalOrder();
}
