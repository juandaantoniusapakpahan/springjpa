package com.backend.springjpa.dto;

public interface StockRiskReport {
    Long getProductId();
    String getProductName();
    Long getVariantId();
    String getSku();
    Integer getStockQty();
    String getRiskLevel();
}
