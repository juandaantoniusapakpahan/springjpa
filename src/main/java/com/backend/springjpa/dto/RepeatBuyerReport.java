package com.backend.springjpa.dto;

import java.time.LocalDate;

public interface RepeatBuyerReport {
    Long getTotalBuyer();
    Long getRepeatBuyer();
    Double getRepeatRate();
}
