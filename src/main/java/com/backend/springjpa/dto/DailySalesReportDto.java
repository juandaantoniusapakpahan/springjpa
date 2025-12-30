package com.backend.springjpa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailySalesReportDto {
    LocalDate getDate();
    Long getTotalOrder();
    BigDecimal getTotalRevenue();
}
