package com.backend.springjpa.repository;

import com.backend.springjpa.dto.SellerBestSellingProductReport;
import com.backend.springjpa.dto.SellerPerformanceReport;
import com.backend.springjpa.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    @Query(value = """
            SELECT p.id as productId, p.name as productName,
            SUM(oi.qty) AS totalQuantity,
            SUM(oi.qty * oi.price) AS totalRevenue FROM sellers s
            JOIN products p ON p.seller_id = s.id
            JOIN product_variants pv ON p.id = pv.product_id
            JOIN order_items oi ON oi.product_variant_id = pv.id
            JOIN orders o ON o.id = oi.order_id
            where s.id = :sellerId AND o.status = 'PAID' AND o.created_at BETWEEN :start AND :end
            GROUP BY p.id, p.name
            ORDER BY totalQuantity DESC
            LIMIT 1
            """, nativeQuery = true)
    SellerBestSellingProductReport getSellerBestSellingProduct(@Param("sellerId") Long sellerId,
                                                               @Param("start")LocalDateTime start,

                                                               @Param("end") LocalDateTime end);

    @Query(value = """
            SELECT s.id AS sellerId,
            s.name AS sellerName,
            SUM(o.total_amount) AS totalRevenue,
            COUNT(DISTINCT o.id) AS totalOrder
             FROM sellers s
            JOIN products p ON p.seller_id = s.id
            JOIN product_variants pv ON pv.product_id = p.id
            JOIN order_items oi ON oi.product_variant_id = pv.id
            JOIN orders o ON o.id = oi.order_id
            WHERE o.status = 'PAID' AND o.created_at BETWEEN :start AND :end
            GROUP BY s.id
            ORDER BY totalRevenue DESC
            """, nativeQuery = true)
    List<SellerPerformanceReport> getSellerPerformanceRanking(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
