package com.backend.springjpa.repository;

import com.backend.springjpa.dto.ProductVariantAverage;
import com.backend.springjpa.dto.StockRiskReport;
import com.backend.springjpa.entity.ProductVariant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @Modifying
    @Query("""
            UPDATE ProductVariant pv
            SET pv.price = pv.price * (1.0 + :percentage)
            WHERE pv.product.id = :productId and pv.deleted = false
            """)
    int updatePriceByPercentage(@Param("productId") Long productId, @Param("percentage")BigDecimal percentage);

    @Modifying
    @Query("""
            Update ProductVariant pv
            SET pv.stockQty = pv.stockQty - :qty
            WHERE pv.id = :id AND pv.stockQty >= :qty
            """)
    int decreaseStock(Long id, int qty);


    @Query(value = """
            SELECT p.id AS productId,
            p.name || ' ' || pv.name AS productName,
            pv.id AS variantId,
            pv.sku AS sku,
            pv.stock_qty stockQty,
            CASE
            WHEN pv.stock_qty <= :highRisk THEN 'HIGH'
            WHEN pv.stock_qty <= :mediumRisk THEN 'MEDIUM'
            ELSE 'SAFE'
            END AS riskLevel
            FROM product_variants pv
            JOIN products p ON p.id = pv.product_id
            WHERE pv.stock_qty  <= :mediumRisk
            ORDER BY pv.stock_qty
            """,nativeQuery = true)
    List<StockRiskReport> getStockRiskReport(
            @Param("highRisk") int highRisk,
            @Param("mediumRisk") int mediumRisk
    );


    @Query(value = """
            SELECT pv.id as id,pv.name AS name,
            pv.sku AS sku, pv.price AS price,
            AVG(r.rating) as averageRate
            FROM product_variants pv
            JOIN reviews r ON r.product_variant_id = pv.id
            WHERE r.product_variant_id = :id AND r.updated = true AND r.created_at BETWEEN :start AND :end
            GROUP BY pv.id
            """, nativeQuery = true)
    ProductVariantAverage getProductVariantAverage(@Param("id") Long id,
                                                   @Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end);



}
