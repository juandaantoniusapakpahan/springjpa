package com.backend.springjpa.repository;

import com.backend.springjpa.entity.ProductVariant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
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

}
