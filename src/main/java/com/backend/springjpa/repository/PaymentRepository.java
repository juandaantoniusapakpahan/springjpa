package com.backend.springjpa.repository;

import com.backend.springjpa.entity.Payment;
import com.backend.springjpa.util.PaymentStatus;
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
}
