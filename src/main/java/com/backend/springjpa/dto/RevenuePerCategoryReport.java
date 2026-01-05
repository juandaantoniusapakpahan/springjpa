package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface RevenuePerCategoryReport {
    String getCategory();
    Long getTotalOrder();
    BigDecimal getTotalRevenue();
}
