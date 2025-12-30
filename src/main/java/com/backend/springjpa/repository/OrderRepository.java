package com.backend.springjpa.repository;

import com.backend.springjpa.dto.DailySalesReportDto;
import com.backend.springjpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = """
            SELECT DATE(o.created_at) as date,
             COUNT(DISTINCT o.id) AS totalOrder,
             SUM(oi.price * oi.qty) as totalRevenue FROM orders o
            JOIN order_items oi on oi.order_id = o.id
            WHERE o.status = 'PAID'
            AND o.created_at BETWEEN :start AND :end
            GROUP BY DATE(o.created_at)
            ORDER BY DATE(o.created_at)
            """,nativeQuery = true)
    List<DailySalesReportDto> getDailySalesReport(
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end
            );
}
