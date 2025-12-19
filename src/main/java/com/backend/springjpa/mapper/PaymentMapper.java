package com.backend.springjpa.mapper;

import com.backend.springjpa.dto.PaymentDto;
import com.backend.springjpa.entity.Payment;

public class PaymentMapper {

    public static PaymentDto toPaymentDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getId().toString());
        dto.setPaymentStatus(payment.getStatus().toString());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}
