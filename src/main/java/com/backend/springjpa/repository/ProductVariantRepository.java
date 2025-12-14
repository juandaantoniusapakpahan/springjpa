package com.backend.springjpa.repository;

import com.backend.springjpa.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @Modifying
    @Query("""
            UPDATE ProductVariant pv
            SET pv.price = pv.price * (1.0 + :percentage)
            WHERE pv.product.id = :productId and pv.deleted = false
            """)
    int updatePriceByPercentage(@Param("productId") Long productId, @Param("percentage")BigDecimal percentage);
}
