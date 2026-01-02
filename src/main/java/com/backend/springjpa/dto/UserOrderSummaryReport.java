package com.backend.springjpa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UserOrderSummaryReport {
    Long getUserId();
    Long getTotalOrder();
    BigDecimal getTotalSpent();
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getLastOrderDate();
}
