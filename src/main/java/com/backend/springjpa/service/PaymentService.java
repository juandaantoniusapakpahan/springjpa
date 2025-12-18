package com.backend.springjpa.service;

import com.backend.springjpa.entity.Payment;
import com.backend.springjpa.repository.OrderRepository;
import com.backend.springjpa.repository.PaymentRepository;
import com.backend.springjpa.util.PaymentStatus;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderService orderService;
    public PaymentService(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expiredPayments() {
        List<Payment> payments = paymentRepository.findExpiredWaitingPayments(PaymentStatus.WAITING,LocalDateTime.now());
        for (Payment payment: payments) {
            payment.setStatus(PaymentStatus.EXPIRED);
            orderService.cancelOrder(payment.getOrder());
        }

    }
}
