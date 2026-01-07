package com.backend.springjpa.service;

import com.backend.springjpa.dto.PaymentDto;
import com.backend.springjpa.dto.PaymentMethodSummaryReport;
import com.backend.springjpa.dto.PaymentStatusSummaryReport;
import com.backend.springjpa.entity.Payment;
import com.backend.springjpa.exception.BadRequestException;
import com.backend.springjpa.exception.ResourceNotFoundException;
import com.backend.springjpa.mapper.PaymentMapper;
import com.backend.springjpa.repository.OrderRepository;
import com.backend.springjpa.repository.PaymentRepository;
import com.backend.springjpa.util.OrderStatus;
import com.backend.springjpa.util.PaymentStatus;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderService orderService;
    public PaymentService(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Transactional
    public void createPayment(PaymentDto dto) {
        Long id = Long.parseLong(dto.getPaymentId());
        Payment payment = paymentRepository.findByIdAndStatus(id, PaymentStatus.WAITING).orElseThrow(()-> new ResourceNotFoundException("Payment not found"));

        if (!PaymentStatus.PAID.toString().equals(dto.getPaymentStatus())){
            payment.setStatus(PaymentStatus.EXPIRED);
            orderService.cancelOrder(payment.getOrder());
            throw new BadRequestException("Internal Error, please try again");
        } else {
            payment.setStatus(PaymentStatus.PAID);
            DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(dto.getTimeStamp(), formatter);
            payment.setPaidAt(localDateTime);
            payment.getOrder().setStatus(OrderStatus.PAID);
            paymentRepository.save(payment);
        }
    }

    public List<PaymentDto> getAllPayment(PaymentStatus status, Pageable pageable) {
        return paymentRepository.findByStatus(status, pageable).stream().map((PaymentMapper::toPaymentDto)).toList();
    }

    public List<PaymentMethodSummaryReport> getPaymentMethodSummary(LocalDate start, LocalDate end) {
        return paymentRepository.getPaymentMethodSummary(start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public List<PaymentStatusSummaryReport> getPaymentStatusSummary(LocalDate start, LocalDate end) {
        return paymentRepository.getPaymentStatusSummary(start.atStartOfDay(), end.atTime(LocalTime.MAX));
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
