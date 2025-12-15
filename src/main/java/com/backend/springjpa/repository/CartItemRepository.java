package com.backend.springjpa.repository;

import com.backend.springjpa.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);
    @Query("""
            Select ci from CartItem ci
            join fetch ci.productVariant pv
            join fetch pv.product p
            where ci.cart.id = :cartId
            """)
    List<CartItem> findByCartId(Long cartId);
}
