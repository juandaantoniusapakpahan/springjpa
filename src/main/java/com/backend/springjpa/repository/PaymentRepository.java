package com.backend.springjpa.repository;

import com.backend.springjpa.dto.PaymentMethodSummaryReport;
import com.backend.springjpa.entity.Payment;
import com.backend.springjpa.util.PaymentStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
            SELECT p FROM Payment p
            WHERE p.status = :status AND p.expiredAt < :now
            """)
    List<Payment> findExpiredWaitingPayments(@Param("status") PaymentStatus status, @Param("now") LocalDateTime now);
    Optional<Payment> findByIdAndStatus (Long id, PaymentStatus status);
    List<Payment> findByStatus(PaymentStatus status, Pageable request);

    @Query(value = """
            SELECT p.method as method,
             COUNT(DISTINCT(p.order_id)) as totalOrder,
             SUM(p.amount) as totalRevenue FROM payments p
            JOIN orders o ON o.id = p.order_id
            WHERE o.created_at BETWEEN :start AND :end
            GROUP BY p.method
            ORDER BY totalRevenue DESC
            """, nativeQuery = true)
    List<PaymentMethodSummaryReport> getPaymentMethodSummary(@Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end);
}
