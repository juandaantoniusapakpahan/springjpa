package com.backend.springjpa.dto;

import com.backend.springjpa.util.PaymentStatus;

import java.math.BigDecimal;

public interface PaymentStatusSummaryReport {
    PaymentStatus getStatus();
    Long getTotalTransaction();
    BigDecimal getTotalAmount();
}
