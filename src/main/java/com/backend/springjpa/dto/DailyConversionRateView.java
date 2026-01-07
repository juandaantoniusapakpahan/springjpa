package com.backend.springjpa.dto;

import java.time.LocalDate;

public interface DailyConversionRateView {
    LocalDate getDate();
    Long getTotalOrder();
    Long getTotalPaid();
    Double getConversionRate();
}
