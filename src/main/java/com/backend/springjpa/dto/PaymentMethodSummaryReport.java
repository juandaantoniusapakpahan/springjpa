package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface PaymentMethodSummaryReport {
    String getMethod();
    Long getTotalOrder();
    BigDecimal getTotalRevenue();
}
