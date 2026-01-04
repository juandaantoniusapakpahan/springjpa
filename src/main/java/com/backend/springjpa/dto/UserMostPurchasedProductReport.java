package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface UserMostPurchasedProductReport {
    Long getProductId();
    String getProductName();
    Long getTotalQuantity();
    BigDecimal getTotalSpent();
}
