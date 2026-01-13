package com.backend.springjpa.dto;

import java.math.BigDecimal;

public interface ProductVariantAverage {
    Long getId();
    String getName();
    String getSku();
    BigDecimal getPrice();
    double getAverageRate();

}
