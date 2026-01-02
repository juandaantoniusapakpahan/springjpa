package com.backend.springjpa.repository;

import com.backend.springjpa.dto.DailySalesReportDto;
import com.backend.springjpa.dto.SellerSalesReport;
import com.backend.springjpa.dto.TopProductVariantReport;
import com.backend.springjpa.dto.UserOrderSummaryReport;
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

    @Query(value = """
            SELECT s.id AS sellerId, s.name as sellerName,  count(DISTINCT o.id) as totalOrder,
             SUM(oi.price * oi.qty) as totalRevenue FROM orders o
            JOIN order_items oi ON oi.order_id = o.id
            JOIN product_variants pv ON pv.id = oi.product_variant_id
            JOIN products p ON p.id = pv.product_id
            JOIN sellers s ON s.id = p.seller_id
            WHERE o.status = 'PAID'
            AND o.created_at BETWEEN :start AND :end
            GROUP BY s.id, s.name ORDER BY totalRevenue DESC
            """, nativeQuery = true)
    List<SellerSalesReport> getSalesPerSeller(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = """
            SELECT pv.id as variantId, pv.sku as sku,
             pv.name as productName, SUM(oi.qty) as totalSold,
             SUM(oi.qty * oi.price) as totalRevenue FROM orders o
            JOIN order_items oi ON oi.order_id = o.id
            JOIN product_variants pv ON pv.id = oi.product_variant_id
            where o.status = 'PAID' AND o.created_at BETWEEN :start AND :end
            GROUP BY pv.id
            ORDER BY totalSold DESC
            """, nativeQuery = true)
    List<TopProductVariantReport> getTopSellingVariants(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = """
            SELECT u.id AS userId,
            COUNT(DISTINCT o.id) AS totalOrder,
            SUM(oi.price * oi.qty) AS totalSpent,
            MAX(o.created_at) as lastOrderDate FROM users u
            JOIN orders o ON o.user_id = u.id
            JOIN order_items oi ON oi.order_id = o.id
            WHERE o.status = 'PAID'
            AND u.id = :userId
            AND o.created_at BETWEEN :start AND :end
            GROUP BY u.id
            """, nativeQuery = true)
    UserOrderSummaryReport getUserOrderSummary(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
