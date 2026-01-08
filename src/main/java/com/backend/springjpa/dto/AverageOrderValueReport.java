package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface AverageOrderValueReport {
    BigDecimal getTotalRevenue();
    Long getTotalOrder();
    BigDecimal getAverageOrderValue();
}
