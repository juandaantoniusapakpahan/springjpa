package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface TopProductVariantReport {
    Long getVariantId();
    String getSku();
    String getProductName();
    Long getTotalSold();
    BigDecimal getTotalRevenue();
}
